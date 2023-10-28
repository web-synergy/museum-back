package baza.trainee.controller.admin;

import baza.trainee.domain.model.User;
import baza.trainee.repository.UserRepository;
import baza.trainee.security.RootUserInitializer;
import baza.trainee.service.ArticleService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
public class AdminChangePasswordTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserRepository userRepository;

    @MockBean
    ArticleService articleService;

    @MockBean
    private RootUserInitializer rootUserInitializer;

    private User user;

    @BeforeEach
    void setUp(){
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

        mockMvc.perform(put("/api/admin/change-password")
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

        mockMvc.perform(put("/api/admin/change-password")
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

        mockMvc.perform(put("/api/admin/change-password")
                        .param("password", password))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

}
