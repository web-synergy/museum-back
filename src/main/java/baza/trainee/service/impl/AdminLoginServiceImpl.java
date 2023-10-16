package baza.trainee.service.impl;

import baza.trainee.domain.model.User;
import baza.trainee.dto.UpdateLoginRequest;
import baza.trainee.exceptions.custom.LoginNotValidException;
import baza.trainee.repository.UserRepository;
import baza.trainee.service.AdminLoginService;
import baza.trainee.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static baza.trainee.constants.MailConstants.ACTIVATION_COD;

@Service
@RequiredArgsConstructor
public class AdminLoginServiceImpl implements AdminLoginService {

    @Value("${mail.museum.messagetimeout}")
    private String messageTimeout;

    private static final String NEW_LOGIN_KEY = "newLogin";
    private static final String VERIFICATION_CODE_KEY = "codeChange";
    private static final int MAX_SIX_DIGIT_CODE = 999999;

    private final MailService mailService;
    private final UserRepository userRepository;
    private final StringRedisTemplate template;

    /**
     * Save setting login for change login, build link
     * and send link to new user email.
     *
     * @param updateLoginRequest Logins for change
     * @param userLogin          Current user login
     */
    @Override
    public void checkAndSaveSettingLogin(UpdateLoginRequest updateLoginRequest, String userLogin) {
        checkMatchesNewLoginAndDuplicate(updateLoginRequest.getNewLogin(),
                updateLoginRequest.getDuplicateNewLogin());
        isNotExistUserByLogin(updateLoginRequest.getNewLogin());
        String code = createConfirmationCode();
        sendEmail(code, updateLoginRequest.getNewLogin());
        saveSettingLogin(userLogin, updateLoginRequest.getNewLogin(), code);
    }

    @Override
    public void approveUpdateLogin(String code, String userLogin) {
        ValueOperations<String, String> opsForValue = template.opsForValue();

        if (!code.equals(opsForValue.get(VERIFICATION_CODE_KEY + "_" + userLogin))) {
            throw new LoginNotValidException("Not valid code");
        }

        User admin = userRepository.findByEmail(userLogin)
                .orElseThrow(() -> new UsernameNotFoundException("Not find user by login"));
        admin.setEmail(opsForValue.getAndDelete(NEW_LOGIN_KEY + "_" + userLogin));
        userRepository.update(admin);

        opsForValue.getAndDelete(VERIFICATION_CODE_KEY + "_" + userLogin);
    }

    private void checkMatchesNewLoginAndDuplicate(String newLogin, String duplicateNewLogin) {
        if (!newLogin.equals(duplicateNewLogin)) {
            throw new LoginNotValidException("Logins do not match");
        }
    }

    private void isNotExistUserByLogin(final String username) {
        userRepository.findByEmail(username).ifPresent(user -> {
            throw new LoginNotValidException("This user already exists in the database");
        });
    }

    private void saveSettingLogin(String userLogin, String newLogin, String code) {
        try {
            template.opsForValue().set(NEW_LOGIN_KEY + "_" + userLogin, newLogin,
                    Long.parseLong(messageTimeout), TimeUnit.MINUTES);
            template.opsForValue().set(VERIFICATION_CODE_KEY + "_" + userLogin, code,
                    Long.parseLong(messageTimeout), TimeUnit.MINUTES);
        } catch (NumberFormatException e) {
            throw new LoginNotValidException("Not parse property timeout");
        }
    }

    private void sendEmail(final String code, final String email) {
        String msgForActivationLogin = mailService.buildMsgForChangeLogin(code);
        mailService.sendEmail(email, msgForActivationLogin, ACTIVATION_COD);
    }

    private String createConfirmationCode() {
        int randomNumber = ThreadLocalRandom.current().nextInt(MAX_SIX_DIGIT_CODE);
        return String.format("%06d", randomNumber);
    }
}
