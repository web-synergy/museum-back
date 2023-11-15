package web.synergy.controller.admin;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import web.synergy.dto.EmailUpdateRequest;
import web.synergy.exceptions.custom.EntityAlreadyExistsException;
import web.synergy.security.RootUserInitializer;
import web.synergy.service.ArticleService;
import web.synergy.service.MuseumDataService;
import web.synergy.service.UserService;

@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
public class AdminUpdateEmailWithoutConfirmationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private ArticleService articleService;

    @MockBean
    private MuseumDataService museumDataService;

    @MockBean
    private RootUserInitializer rootUserInitializer;

    @SneakyThrows
    @Test
    @WithMockUser(username = "current_email@mail.com", authorities = "SCOPE_WRITE")
    void updateEmailWithoutConfirm_Ok() {
        String emailToUpdate = "new_email@mail.com";
        var updateLoginRequest = new EmailUpdateRequest(emailToUpdate);

        mockMvc.perform(put("/api/admin/update/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateLoginRequest)))
                .andExpect(status().isNoContent());

        verify(userService).updateEmail("current_email@mail.com", emailToUpdate);
    }

    @SneakyThrows
    @WithMockUser(username = "current_email@mail.com", authorities = "SCOPE_WRITE")
    @ParameterizedTest
    @ValueSource(strings = { "", "not_an_email" })
    void updateEmailWithoutConfirm_InvalidEmail_BadRequest(String emailToUpdate) {
        var updateLoginRequest = new EmailUpdateRequest(emailToUpdate);

        mockMvc.perform(put("/api/admin/update/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateLoginRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).updateEmail("current_email@mail.com", emailToUpdate);
    }

    @SneakyThrows
    @Test
    @WithMockUser(username = "current_email@mail.com", authorities = "SCOPE_WRITE")
    void updateEmailWithoutConfirm_EmailIsTaken_BadRequest() {
        String emailToUpdate = "new_email@mail.com";
        var updateLoginRequest = new EmailUpdateRequest(emailToUpdate);

        doThrow(new EntityAlreadyExistsException("User", emailToUpdate)).when(userService).updateEmail(anyString(), anyString());

        mockMvc.perform(put("/api/admin/update/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateLoginRequest)))
                .andExpect(status().isBadRequest());

        verify(userService).updateEmail("current_email@mail.com", emailToUpdate);
    }

    @SneakyThrows
    @Test
    @WithMockUser(username = "current_email@mail.com")
    void updateEmailWithoutConfirm_Forbidden() {
        String emailToUpdate = "new_email@mail.com";
        var updateLoginRequest = new EmailUpdateRequest(emailToUpdate);

        mockMvc.perform(put("/api/admin/update/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateLoginRequest)))
                .andExpect(status().isForbidden());

        verify(userService, never()).updateEmail("current_email@mail.com", emailToUpdate);
    }

    @SneakyThrows
    @Test
    void updateEmailWithoutConfirm_Unauthorized() {
        String emailToUpdate = "new_email@mail.com";
        var updateLoginRequest = new EmailUpdateRequest(emailToUpdate);

        mockMvc.perform(put("/api/admin/update/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateLoginRequest)))
                .andExpect(status().isUnauthorized());

        verify(userService, never()).updateEmail("current_email@mail.com", emailToUpdate);
    }
}
