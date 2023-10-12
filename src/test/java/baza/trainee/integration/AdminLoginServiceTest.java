package baza.trainee.integration;

import baza.trainee.domain.model.User;
import baza.trainee.dto.LoginDto;
import baza.trainee.exceptions.custom.LoginNotValidException;
import baza.trainee.repository.UserRepository;
import baza.trainee.service.MailService;
import baza.trainee.service.impl.AdminLoginServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Optional;

import static baza.trainee.constants.MailConstants.ACTIVATION_COD;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class AdminLoginServiceTest extends AbstractIntegrationTest{
    String oldLogin = "oldLogin@email.com";
    String newLogin = "newLogin@email.com";
    String VERIFICATION_CODE_KEY = "codeChange";
    String OLD_LOGIN_KEY = "oldLogin";
    String NEW_LOGIN_KEY = "newLogin";
    String verificationCodeValue = "12345";

    @Autowired
    private AdminLoginServiceImpl adminLoginService;
    @Autowired
    private RedisTemplate<String, String> template;

    @MockBean
    private MailService mailService;
    @MockBean
    private UserRepository userRepository;


    @Test
    void checkLoginIsSuccessful() {
        assertDoesNotThrow(() -> adminLoginService
                .checkLogin(oldLogin, oldLogin));
    }
    @Test
    void checkLoginIsFail() {
        assertThrows(LoginNotValidException.class, () ->adminLoginService
                .checkLogin(newLogin, newLogin));
    }

    @Test
    void checkAndSaveSettingLogin() {
        LoginDto loginDto = new LoginDto(oldLogin, newLogin,
                newLogin);
        when(userRepository.findByEmail(newLogin)).thenReturn(Optional.of(new User()));

        String message = "Code 123456";
        when(mailService.buildMsgForChangeLogin(verificationCodeValue)).thenReturn(message);

        verify(mailService, times(1)).sendEmail(newLogin, message, ACTIVATION_COD);

        assertDoesNotThrow(() -> adminLoginService.checkAndSaveSettingLogin(loginDto,
                oldLogin));
        assertEquals(template.opsForValue().get(OLD_LOGIN_KEY), oldLogin);
        assertEquals(template.opsForValue().get(NEW_LOGIN_KEY), newLogin);
        assertEquals(template.opsForValue().get(VERIFICATION_CODE_KEY), verificationCodeValue);
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

        user.setEmail(newLogin);
        verify(userRepository, times(1)).save(user);
    }
}