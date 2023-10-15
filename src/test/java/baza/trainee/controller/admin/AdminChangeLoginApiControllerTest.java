package baza.trainee.controller.admin;

import baza.trainee.dto.LoginDto;
import baza.trainee.security.RootUserInitializer;
import baza.trainee.service.AdminLoginService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
class AdminChangeLoginApiControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AdminLoginService adminLoginService;

    @MockBean
    private RootUserInitializer rootUserInitializer;

    @WithMockUser(roles = {"ADMIN"})
    @SneakyThrows
    @Test
    void changeCurrentLoginToNewLogin() {
        String code = "123456";
        mockMvc.perform(put("/api/admin/changeLogin")
                        .param("code", code))
                .andDo(print()).andExpect(status().isNoContent());
    }


    @WithMockUser(roles = {"ADMIN"})
    @SneakyThrows
    @Test
    void saveSettingForChangeLogin() {
        LoginDto loginDto = new LoginDto(
                "newLogin@email.com",
                "newLogin@email.com");
        mockMvc.perform(post("/api/admin/saveSettingLogin")
                        .requestAttr("loginDto", loginDto))
                .andDo(print()).andExpect(status().isNoContent());
    }

    @SneakyThrows
    @Test
    void changeCurrentLoginToNewLoginNotAuthorizeUser() {
        String code = "123456";
        mockMvc.perform(put("/api/admin/changeLogin")
                        .param("code", code))
                .andDo(print()).andExpect(status().isUnauthorized());
    }

    @SneakyThrows
    @Test
    void saveSettingsNotAuthorizeUser() {
        LoginDto loginDto = new LoginDto("newLogin@email.com",
                "newLogin@email.com");
        mockMvc.perform(post("/api/admin/saveSettingLogin")
                        .requestAttr("loginDto", loginDto))
                .andDo(print()).andExpect(status().isUnauthorized());
    }
}