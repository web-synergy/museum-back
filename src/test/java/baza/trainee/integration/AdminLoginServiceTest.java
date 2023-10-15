package baza.trainee.integration;

import baza.trainee.domain.model.User;
import baza.trainee.dto.UpdateLoginRequest;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static baza.trainee.constants.MailConstants.ACTIVATION_COD;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class AdminLoginServiceTest extends AbstractIntegrationTest {
    String oldLogin = "oldLogin@email.com";
    String newLogin = "newLogin@email.com";
    String VERIFICATION_CODE_KEY = "codeChange";
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
    void checkAndSaveSettingLogin() {
        UpdateLoginRequest updateLoginRequest = new UpdateLoginRequest(newLogin, newLogin);
        when(userRepository.findByEmail(newLogin)).thenReturn(Optional.empty());

        String message = "Code 123456";
        when(mailService.buildMsgForChangeLogin(anyString())).thenReturn(message);

        assertDoesNotThrow(() -> adminLoginService.checkAndSaveSettingLogin(updateLoginRequest,
                oldLogin));

        verify(mailService, times(1)).sendEmail(newLogin, message, ACTIVATION_COD);

        assertAll("Test keys",
                () -> assertEquals(template.opsForValue().get(NEW_LOGIN_KEY + "_" + oldLogin), newLogin),
                () -> assertNotNull(template.opsForValue().get(VERIFICATION_CODE_KEY + "_" + oldLogin)));
    }

    @Test
    void checkAndSaveSettingLoginNotMatchesNewLoginAndDuplicate() {
        UpdateLoginRequest loginDto = new UpdateLoginRequest(newLogin,oldLogin);

        assertThrows(LoginNotValidException.class,
                () -> adminLoginService.checkAndSaveSettingLogin(loginDto, oldLogin));
    }

    @Test
    void approveUpdateLogin() {
        ValueOperations<String, String> opsForValue = template.opsForValue();
        opsForValue.set(NEW_LOGIN_KEY + "_" + oldLogin, newLogin);
        opsForValue.set(VERIFICATION_CODE_KEY + "_" + oldLogin, verificationCodeValue);
        User user = new User();

        when(userRepository.findByEmail(oldLogin)).thenReturn(Optional.of(user));
        assertDoesNotThrow(() -> adminLoginService.approveUpdateLogin(verificationCodeValue, oldLogin));

        verify(userRepository, times(1)).update(user);

        assertAll("Test keys",
                () -> assertNull(template.opsForValue().get(NEW_LOGIN_KEY + "_" + oldLogin)),
                () -> assertNull(template.opsForValue().get(VERIFICATION_CODE_KEY + "_" + oldLogin)));
    }

    @Test
    void approveUpdateLoginNotValidCode() {
        String fakeCode = "000000";
        ValueOperations<String, String> opsForValue = template.opsForValue();
        opsForValue.set(VERIFICATION_CODE_KEY + "_" + oldLogin, verificationCodeValue);

        assertThrows(LoginNotValidException.class,
                () -> adminLoginService.approveUpdateLogin(fakeCode, oldLogin));
    }

    @Test
    void approveUpdateLoginNotValidUser() {
        ValueOperations<String, String> opsForValue = template.opsForValue();
        opsForValue.set(NEW_LOGIN_KEY + "_" + oldLogin, newLogin);
        opsForValue.set(VERIFICATION_CODE_KEY + "_" + oldLogin, verificationCodeValue);

        when(userRepository.findByEmail(oldLogin)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> adminLoginService.approveUpdateLogin(verificationCodeValue, oldLogin));
    }
}