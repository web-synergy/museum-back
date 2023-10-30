package web.synergy.integration;

import web.synergy.domain.model.User;
import web.synergy.exceptions.custom.LoginNotValidException;
import web.synergy.repository.UserRepository;
import web.synergy.service.MailService;
import web.synergy.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static web.synergy.domain.enums.OpsKey.CONFIRM_CODE_KEY;
import static web.synergy.domain.enums.OpsKey.EMAIL_KEY;
import static web.synergy.domain.enums.Templates.UPDATE_LOGIN;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class UpdateEmailIntegrationTest extends AbstractIntegrationTest {
    String oldLogin = "oldLogin@email.com";
    String newLogin = "newLogin@email.com";
    String verificationCodeValue = "12345";

    @Autowired
    private UserService userService;

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
        when(userRepository.findByEmail(newLogin)).thenReturn(Optional.empty());

        String message = "Code 123456";
        when(mailService.buildHTMLMessageContent(eq(UPDATE_LOGIN), anyString())).thenReturn(message);

        assertDoesNotThrow(() -> userService.updateEmail(newLogin,
                oldLogin));

        assertAll("Test keys",
                () -> assertEquals(template.opsForValue().get(EMAIL_KEY + "_" + oldLogin), newLogin),
                () -> assertNotNull(template.opsForValue().get(CONFIRM_CODE_KEY + "_" + oldLogin)));
    }

    @Test
    void approveUpdateLogin() {
        ValueOperations<String, String> opsForValue = template.opsForValue();
        opsForValue.set(EMAIL_KEY + "_" + oldLogin, newLogin);
        opsForValue.set(CONFIRM_CODE_KEY + "_" + oldLogin, verificationCodeValue);
        User user = new User();

        when(userRepository.findByEmail(oldLogin)).thenReturn(Optional.of(user));
        assertDoesNotThrow(() -> userService.confirmUpdateEmail(verificationCodeValue, oldLogin));

        verify(userRepository, times(1)).update(user);

        assertEquals(user.getEmail(), newLogin);

        assertAll("Test keys",
                () -> assertNull(template.opsForValue().get(EMAIL_KEY + "_" + oldLogin)),
                () -> assertNull(template.opsForValue().get(CONFIRM_CODE_KEY + "_" + oldLogin)));
    }

    @Test
    void approveUpdateLoginNotValidCode() {
        String fakeCode = "000000";
        ValueOperations<String, String> opsForValue = template.opsForValue();
        opsForValue.set(CONFIRM_CODE_KEY + "_" + oldLogin, verificationCodeValue);

        assertThrows(LoginNotValidException.class,
                () -> userService.confirmUpdateEmail(fakeCode, oldLogin));
    }

    @Test
    void approveUpdateLoginNotValidUser() {
        ValueOperations<String, String> opsForValue = template.opsForValue();
        opsForValue.set(EMAIL_KEY + "_" + oldLogin, newLogin);
        opsForValue.set(CONFIRM_CODE_KEY + "_" + oldLogin, verificationCodeValue);

        when(userRepository.findByEmail(oldLogin)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userService.confirmUpdateEmail(verificationCodeValue, oldLogin));
    }
}