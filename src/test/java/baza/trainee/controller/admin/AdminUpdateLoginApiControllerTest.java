package baza.trainee.controller.admin;

import baza.trainee.domain.enums.Templates;
import baza.trainee.dto.EmailUpdateRequest;
import baza.trainee.security.RootUserInitializer;
import baza.trainee.service.ArticleService;
import baza.trainee.service.MailService;
import baza.trainee.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
class AdminUpdateLoginApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private MailService mailService;

    @MockBean
    private ArticleService articleService;

    @MockBean
    private RootUserInitializer rootUserInitializer;

    @SneakyThrows
    @Test
    void changeCurrentLoginToNewLogin() {
        String code = "123456";

        mockMvc.perform(put("/api/admin/update/confirm-email")
                        .param("code", code)
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_WRITE"))))
                .andExpect(status().isNoContent());
    }

    @SneakyThrows
    @Test
    void saveSettingForChangeLogin() {
        var updateLoginRequest = new EmailUpdateRequest("new_login@email.com");

        when(mailService.buildHTMLMessageContent(eq(Templates.UPDATE_LOGIN), anyString()))
                .thenReturn("CONFIRMATION");

        mockMvc.perform(post("/api/admin/update/email")
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_WRITE")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateLoginRequest)))
                .andExpect(status().isNoContent());
    }

    @SneakyThrows
    @Test
    void changeCurrentLoginToNewLoginNotAuthorizeUser() {
        String code = "123456";
        mockMvc.perform(put("/api/admin/update/confirm-email")
                        .param("code", code))
                .andExpect(status().isUnauthorized());
    }

    @SneakyThrows
    @Test
    void saveSettingsNotAuthorizeUser() {
        var updateLoginRequest = new EmailUpdateRequest("newLogin@email.com");

        mockMvc.perform(post("/api/admin/update/confirm-email")
                        .content(objectMapper.writeValueAsString(updateLoginRequest)))
                .andExpect(status().isUnauthorized());
    }
}