package baza.trainee.service;

import baza.trainee.dto.LoginDto;
import baza.trainee.exceptions.custom.LoginNotValidException;
import baza.trainee.repository.UserRepository;
import baza.trainee.security.RootUserInitializer;
import baza.trainee.service.impl.AdminLoginServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminLoginServiceTest {
    @Autowired
    private AdminLoginServiceImpl adminLoginService;
    @MockBean
    private RootUserInitializer rootUserInitializer;
    @MockBean
    private MailService mailService;
    @MockBean
    private UserRepository userRepository;

    @Test
    void checkLoginIsSuccessful() {
        assertDoesNotThrow(() -> adminLoginService
                .checkLogin("user@email.com",
                        "user@email.com"));
    }
    @Test
    void checkLoginIsFail() {
        assertThrows(LoginNotValidException.class, () ->adminLoginService
                .checkLogin("fail@email.com","user@email.com"));
    }

    @Test
    void checkAndSaveSettingLogin() {
        LoginDto loginDto = new LoginDto("oldLogin@email.com", "newLogin@email.com",
                "newLogin@email.com");
        MockHttpSession session = new MockHttpSession(null, "session123");
        assertDoesNotThrow(() -> adminLoginService.checkAndSaveSettingLogin(loginDto,
                "oldLogin@email.com", session));
    }

    @Test
    void changeLogin() {
        MockHttpSession session = new MockHttpSession(null, "session123");
        session.setAttribute("cod", "123456");
        assertDoesNotThrow(() ->adminLoginService.changeLogin("123456", session));
    }
}