package baza.trainee.service.impl;

import baza.trainee.domain.model.User;
import baza.trainee.dto.LoginDto;
import baza.trainee.exceptions.custom.LoginNotValidException;
import baza.trainee.repository.UserRepository;
import baza.trainee.service.AdminLoginService;
import baza.trainee.service.MailService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static baza.trainee.constants.MailConstants.ACTIVATION_COD;

@Service
@RequiredArgsConstructor
public class AdminLoginServiceImpl implements AdminLoginService {

    private static final String OLD_LOGIN_KEY = "oldLogin";
    private static final String NEW_LOGIN_KEY = "newLogin";
    private static final String VERIFICATION_CODE_KEY = "codChange";
    private static final int MAX_SIX_DIGIT_CODE = 999999;

    private final MailService mailService;
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> template;


    /**
     * Check login.
     *
     * @param enteredLogin    Login for check
     * @param userLogin Current user
     */
    @Override
    public void checkLogin(String enteredLogin, String userLogin) {
        if (!enteredLogin.equals(userLogin)) {
            throw new LoginNotValidException("Entered old login is not valid");
        }
    }

    private void checkMatchesNewLoginAndDuplicate(String newLogin, String duplicateNewLogin) {
        if (!newLogin.equals(duplicateNewLogin)) {
            throw new LoginNotValidException("Logins do not match");
        }
    }

    /**
     * Save setting login for change login, build link
     * and send link to new user email.
     *
     * @param loginDto    Logins for change
     * @param userLogin Current user login
     * @param session     {@link HttpSession}
     */
    @Override
    public void checkAndSaveSettingLogin(LoginDto loginDto, String userLogin,
                                         HttpSession session) {
        checkLogin(loginDto.getOldLogin(), userLogin);
        checkMatchesNewLoginAndDuplicate(loginDto.getNewLogin(),
                loginDto.getDuplicateNewLogin());
        checkDuplicateLoginInBase(loginDto.getNewLogin());
        String code = createCodeChange();
        sendEmail(code, loginDto.getNewLogin());
        saveSettingLogin(loginDto, code);
    }

    private void checkDuplicateLoginInBase(final String username) {
        if (userRepository.findByEmail(username).isPresent()) {
            throw new LoginNotValidException("This user already exists in the database");
        }
    }

    @Override
    public void changeLogin(String code, HttpSession session) {
        if (!code.equals(session.getAttribute(VERIFICATION_CODE_KEY))) {
            throw new LoginNotValidException("Not valid code");
        }
        User admin = userRepository.findByEmail(
                (String) session.getAttribute(OLD_LOGIN_KEY))
                .orElseThrow(() -> new LoginNotValidException("Not find user by login"));
        admin.setEmail((String) session.getAttribute(NEW_LOGIN_KEY));
        userRepository.save(admin);

    }

    private void saveSettingLogin(LoginDto loginDto, String code) {
       /* template.
        session.setAttribute(OLD_LOGIN_KEY, loginDto.getOldLogin());
        session.setAttribute(NEW_LOGIN_KEY, loginDto.getNewLogin());
        session.setAttribute(VERIFICATION_CODE_KEY, code);*/
    }

    private void sendEmail(final String code, @Email String email) {
        String msgForActivationLogin = mailService.buildMsgForChangeLogin(code);
        mailService.sendEmail(email, msgForActivationLogin, ACTIVATION_COD);
    }

    private String createCodeChange() {
        int randomNumber = ThreadLocalRandom.current().nextInt(MAX_SIX_DIGIT_CODE);
        return String.format("%06d", randomNumber);
    }
}
