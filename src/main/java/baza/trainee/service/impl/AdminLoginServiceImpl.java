package baza.trainee.service.impl;

import baza.trainee.domain.model.User;
import baza.trainee.dto.LoginDto;
import baza.trainee.exceptions.custom.LoginNotValidException;
import baza.trainee.repository.UserRepository;
import baza.trainee.service.AdminLoginService;
import baza.trainee.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

import static baza.trainee.constants.MailConstants.ACTIVATION_COD;

@Service
@RequiredArgsConstructor
public class AdminLoginServiceImpl implements AdminLoginService {

    private static final String OLD_LOGIN_KEY = "oldLogin";
    private static final String NEW_LOGIN_KEY = "newLogin";
    private static final String VERIFICATION_CODE_KEY = "codeChange";
    private static final int MAX_SIX_DIGIT_CODE = 999999;

    private final MailService mailService;
    private final UserRepository userRepository;
    private final StringRedisTemplate template;

    private void checkMatchesNewLoginAndDuplicate(String newLogin, String duplicateNewLogin) {
        if (!newLogin.equals(duplicateNewLogin)) {
            throw new LoginNotValidException("Logins do not match");
        }
    }

    /**
     * Save setting login for change login, build link
     * and send link to new user email.
     *
     * @param loginDto  Logins for change
     * @param userLogin Current user login
     */
    @Override
    public void checkAndSaveSettingLogin(LoginDto loginDto, String userLogin) {
        checkMatchesNewLoginAndDuplicate(loginDto.getNewLogin(),
                loginDto.getDuplicateNewLogin());
        isNotExistUserByLogin(loginDto.getNewLogin());
        String code = createCodeChange();
        sendEmail(code, loginDto.getNewLogin());
        saveSettingLogin(loginDto, code);
    }

    private void isNotExistUserByLogin(final String username) {
        if (userRepository.findByEmail(username).isPresent()) {
            throw new LoginNotValidException("This user already exists in the database");
        }
    }

    @Override
    public void changeLogin(String code) {
        ValueOperations<String, String> opsForValue = template.opsForValue();
        if (!code.equals(opsForValue.get(VERIFICATION_CODE_KEY))) {
            throw new LoginNotValidException("Not valid code");
        }
        User admin = userRepository.findByEmail(
                        opsForValue.get(OLD_LOGIN_KEY))
                .orElseThrow(() -> new LoginNotValidException("Not find user by login"));
        admin.setEmail(opsForValue.get(NEW_LOGIN_KEY));
        userRepository.save(admin);

    }

    private void saveSettingLogin(LoginDto loginDto, String code) {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        template.opsForValue().set(OLD_LOGIN_KEY, username);
        template.opsForValue().set(NEW_LOGIN_KEY, loginDto.getNewLogin());
        template.opsForValue().set(VERIFICATION_CODE_KEY, code);
    }

    private void sendEmail(final String code, final String email) {
        String msgForActivationLogin = mailService.buildMsgForChangeLogin(code);
        mailService.sendEmail(email, msgForActivationLogin, ACTIVATION_COD);
    }

    private String createCodeChange() {
        int randomNumber = ThreadLocalRandom.current().nextInt(MAX_SIX_DIGIT_CODE);
        return String.format("%06d", randomNumber);
    }
}
