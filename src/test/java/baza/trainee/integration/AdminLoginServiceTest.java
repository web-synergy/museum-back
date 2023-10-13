package baza.trainee.integration;

import baza.trainee.domain.model.User;
import baza.trainee.dto.LoginDto;
import baza.trainee.exceptions.custom.EmailSendingException;
import baza.trainee.exceptions.custom.LoginNotValidException;
import baza.trainee.repository.UserRepository;
import baza.trainee.service.MailService;
import baza.trainee.service.impl.AdminLoginServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Optional;

import static baza.trainee.constants.MailConstants.ACTIVATION_COD;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class AdminLoginServiceTest extends AbstractIntegrationTest {
    String oldLogin = "oldLogin@email.com";
    String newLogin = "newLogin@email.com";
    String VERIFICATION_CODE_KEY = "codeChange";
    String OLD_LOGIN_KEY = "oldLogin";
    String NEW_LOGIN_KEY = "newLogin";
    String verificationCodeValue = "12345";

    @Autowired
    private AdminLoginServiceImpl adminLoginService;
    @Autowired
    private StringRedisTemplate template;

    @MockBean
    private MailService mailService;
    @MockBean
    HttpServletRequest httpServletRequest;
    @MockBean
    private UserRepository userRepository;


    @Test
    void checkLoginIsSuccessful() {
        assertDoesNotThrow(() -> adminLoginService
                .checkLogin(oldLogin, oldLogin));
    }

    @Test
    void checkLoginIsFail() {
        assertThrows(LoginNotValidException.class, () -> adminLoginService
                .checkLogin(newLogin, oldLogin));
    }

    @Test
    void checkAndSaveSettingLogin() {
        LoginDto loginDto = new LoginDto(oldLogin, newLogin,
                newLogin);
        when(userRepository.findByEmail(newLogin)).thenReturn(Optional.empty());

        String message = "Code 123456";
        when(mailService.buildMsgForChangeLogin(anyString())).thenReturn(message);

        assertDoesNotThrow(() -> adminLoginService.checkAndSaveSettingLogin(loginDto,
                oldLogin));

        verify(mailService, times(1)).sendEmail(newLogin, message, ACTIVATION_COD);

        assertAll("Test keys",
                () -> assertEquals(template.opsForValue().get(OLD_LOGIN_KEY), oldLogin),
                () -> assertEquals(template.opsForValue().get(NEW_LOGIN_KEY), newLogin),
                () -> assertNotNull(template.opsForValue().get(VERIFICATION_CODE_KEY)));

    }

    @Test
    void checkAndSaveSettingLoginNotValidOldLogin() {
        LoginDto loginDto = new LoginDto(newLogin, newLogin,
                newLogin);
        assertThrows(LoginNotValidException.class,
                () -> adminLoginService.checkAndSaveSettingLogin(loginDto, oldLogin));
    }

    @Test
    void checkAndSaveSettingLoginNotMatchesNewLoginAndDuplicate() {
        LoginDto loginDto = new LoginDto(oldLogin, newLogin,
                oldLogin);
        assertThrows(LoginNotValidException.class,
                () -> adminLoginService.checkAndSaveSettingLogin(loginDto, oldLogin));
    }

    @Test
    void checkAndSaveSettingLoginExistUser() {
        LoginDto loginDto = new LoginDto(oldLogin, newLogin,
                newLogin);
        when(userRepository.findByEmail(newLogin)).thenReturn(Optional.of(new User()));
        assertThrows(LoginNotValidException.class,
                () -> adminLoginService.checkAndSaveSettingLogin(loginDto, oldLogin));
    }

    @Test
    void checkAndSaveSettingLoginNotSendLetter() {
        LoginDto loginDto = new LoginDto(oldLogin, newLogin,
                newLogin);
        when(userRepository.findByEmail(newLogin)).thenReturn(Optional.empty());

        String message = "Code 123456";
        when(mailService.buildMsgForChangeLogin(anyString())).thenReturn(message);
        doThrow(EmailSendingException.class).when(mailService)
                .sendEmail(newLogin, message, ACTIVATION_COD);
        assertThrows(EmailSendingException.class,
                () -> adminLoginService.checkAndSaveSettingLogin(loginDto, oldLogin));
    }

    @Test
    void changeLogin() {
        ValueOperations<String, String> opsForValue = template.opsForValue();
        opsForValue.set(OLD_LOGIN_KEY, oldLogin);
        opsForValue.set(NEW_LOGIN_KEY, newLogin);
        opsForValue.set(VERIFICATION_CODE_KEY, verificationCodeValue);
        User user = new User();

        when(userRepository.findByEmail(oldLogin)).thenReturn(Optional.of(user));
        assertDoesNotThrow(() -> adminLoginService.changeLogin(verificationCodeValue));

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void changeLoginNotValidCode() {
        String fakeCode = "000000";
        ValueOperations<String, String> opsForValue = template.opsForValue();
        opsForValue.set(VERIFICATION_CODE_KEY, verificationCodeValue);

        assertThrows(LoginNotValidException.class,
                () -> adminLoginService.changeLogin(fakeCode));

    }

    @Test
    void changeLoginNotValidUser() {
        ValueOperations<String, String> opsForValue = template.opsForValue();
        opsForValue.set(OLD_LOGIN_KEY, oldLogin);
        opsForValue.set(NEW_LOGIN_KEY, newLogin);
        opsForValue.set(VERIFICATION_CODE_KEY, verificationCodeValue);

        when(userRepository.findByEmail(oldLogin)).thenReturn(Optional.empty());

        assertThrows(LoginNotValidException.class,
                () -> adminLoginService.changeLogin(verificationCodeValue));

    }
}