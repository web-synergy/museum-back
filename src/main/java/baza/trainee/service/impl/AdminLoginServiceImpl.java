package baza.trainee.service.impl;

import baza.trainee.domain.dto.LoginDto;
import baza.trainee.domain.model.User;
import baza.trainee.exceptions.custom.LoginNotValidException;
import baza.trainee.repository.UserRepository;
import baza.trainee.service.AdminLoginService;
import baza.trainee.service.MailService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Random;

import static baza.trainee.constants.MailConstants.ACTIVATION_COD;

@Service
@RequiredArgsConstructor
public class AdminLoginServiceImpl implements AdminLoginService {
    private static final String OLD_LOGIN = "oldLogin";
    private static final String NEW_LOGIN = "newLogin";
    private static final String CODE_CHANGE = "codChange";
    private static final int SIX_DIGIT_CODE = 999999;
    private final MailService mailService;
    private final UserRepository userRepository;
    private final Random random = new Random();

    /**
     * Check login.
     *
     * @param oldLogin    Login for check
     * @param userDetails Current user
     */
    @Override
    public void checkLogin(String oldLogin, UserDetails userDetails) {
        if (!oldLogin.equals(userDetails.getUsername())) {
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
     * @param userDetails Current user
     * @param session     {@link HttpSession}
     */
    @Override
    public void checkAndSaveSettingLogin(LoginDto loginDto, UserDetails userDetails,
                                         HttpSession session) {
        checkLogin(loginDto.oldLogin(), userDetails);
        checkMatchesNewLoginAndDuplicate(loginDto.newLogin(), loginDto.duplicateNewLogin());
        checkDuplicateLoginInBase(loginDto.newLogin());
        String cod = createCodeChange();
        sendEmail(cod, loginDto.newLogin());
        saveSettingLogin(loginDto, cod, session);
    }

    private void checkDuplicateLoginInBase(final String login) {
        if (userRepository.findByEmail(login).isEmpty()) {
            throw new LoginNotValidException("This user already exists in the database");
        }
    }

    @Override
    public void changeLogin(String cod, HttpSession session) {
        if (!cod.equals(session.getAttribute(CODE_CHANGE))) {
            throw new LoginNotValidException("Not valid cod");
        }
        User admin = userRepository.findByEmail(
                (String) session.getAttribute(OLD_LOGIN))
                .orElseThrow(() -> new LoginNotValidException("Not find user by login"));
        admin.setEmail((String) session.getAttribute(NEW_LOGIN));
        userRepository.save(admin);

    }

    private void saveSettingLogin(LoginDto loginDto, String code, HttpSession session) {
        session.setAttribute(OLD_LOGIN, loginDto.oldLogin());
        session.setAttribute(NEW_LOGIN, loginDto.newLogin());
        session.setAttribute(CODE_CHANGE, code);
    }

    private void sendEmail(final String code, @Email String email) {
        String msgForActivationLogin = mailService.buildMsgForChangeLogin(code);
        mailService.sendEmail(email, msgForActivationLogin, ACTIVATION_COD);
    }

    private String createCodeChange() {
        int randomNumber = random.nextInt(SIX_DIGIT_CODE);
        return String.format("%06d", randomNumber);
    }
}
