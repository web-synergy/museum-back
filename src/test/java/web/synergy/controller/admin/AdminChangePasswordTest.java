package web.synergy.controller.admin;

import web.synergy.domain.model.User;
import web.synergy.repository.UserRepository;
import web.synergy.security.RootUserInitializer;
import web.synergy.service.ArticleService;
import web.synergy.service.MailService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import web.synergy.service.MuseumDataService;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
class AdminChangePasswordTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private MailService mailService;

    @MockBean
    private ArticleService articleService;

    @MockBean
    private MuseumDataService museumDataService;

    @MockBean
    private RootUserInitializer rootUserInitializer;

    @Captor
    private ArgumentCaptor<String> stringCaptor;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setEmail("email@gmail.com");
        user.setPassword("old_password");
    }

    @Test
    @SneakyThrows
    void updatePasswordWithValidData_ShouldReturnNoContent() {
        String password = "new_password";

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        mockMvc.perform(put("/api/admin/update/password")
                        .param("password", password)
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_WRITE"))))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @SneakyThrows
    void updatePasswordWithInValidData_ShouldReturnBadRequest() {
        String password = null;

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        mockMvc.perform(put("/api/admin/update/password")
                        .param("password", password)
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_WRITE"))))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void updatePasswordWithoutAuthentication_ShouldReturnUnauthorized() {
        String password = "new_password";

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        mockMvc.perform(put("/api/admin/update/password")
                        .param("password", password))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    void recoveryPasswordWithValidEmail_ShouldReturnNoContent() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(mailService.buildHTMLMessageContent(any(), anyString())).thenReturn(anyString());

        mockMvc.perform(put("/api/admin/update/recovery-password")
                        .param("email", user.getEmail()))
                .andExpect(status().isNoContent());

        verify(userRepository).update(any());
        verify(mailService).sendEmail(eq(user.getEmail()), anyString(), anyString());
    }

    @Test
    @SneakyThrows
    void recoveryPasswordWithNotExistingEmail_ShouldReturnNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(mailService.buildHTMLMessageContent(any(), anyString())).thenReturn(anyString());

        mockMvc.perform(put("/api/admin/update/recovery-password")
                        .param("email", user.getEmail()))
                .andExpect(status().isNotFound());

        verify(userRepository, never()).update(any());
        verify(mailService, never()).sendEmail(eq(user.getEmail()), anyString(), anyString());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"_(#&&@%$$"})
    @SneakyThrows
    void recoveryPasswordWithInvalidEmail_ShouldReturnBadRequest(String email) {
        mockMvc.perform(put("/api/admin/update/recovery-password")
                        .param("email", email))
                .andExpect(status().isBadRequest());

        verify(userRepository, never()).update(any());
        verify(mailService, never()).sendEmail(eq(user.getEmail()), anyString(), anyString());
    }

    @Test
    @SneakyThrows
    void updatedPasswordAndSendPassword_AreEquals() {
        String expected;
        String actual;

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(mailService.buildHTMLMessageContent(any(), anyString())).thenReturn(anyString());

        mockMvc.perform(put("/api/admin/update/recovery-password")
                        .param("email", user.getEmail()))
                .andExpect(status().isNoContent());

        verify(mailService).buildHTMLMessageContent(any(), stringCaptor.capture());
        expected = stringCaptor.getValue();


        verify(userRepository).update(userCaptor.capture());
        actual = userCaptor.getValue().getPassword();

        assertEquals(8, expected.length());
        assertTrue(passwordEncoder.matches(expected, actual));
    }
}
