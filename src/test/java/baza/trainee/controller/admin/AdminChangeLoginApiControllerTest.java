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
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
@WithMockUser(roles = {"ADMIN"})
class AdminChangeLoginApiControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AdminLoginService adminLoginService;

    @MockBean
    private RootUserInitializer rootUserInitializer;

    @SneakyThrows
    @Test
    void changeLogin() {
        mockMvc.perform(put("/admin/changeLogin")
                        .requestAttr("code", "123456"))
                .andDo(print()).andExpect(status().isNoContent());
    }

    @SneakyThrows
    @Test
    void checkLogin() {
        mockMvc.perform(get("/admin/checkOldLogin")
                        .param("oldLogin", "oldLogin@email.com"))
                .andDo(print()).andExpect(status().isNoContent());
    }

    @SneakyThrows
    @Test
    void saveLogin() {
        LoginDto loginDto = new LoginDto("oldLogin@email.com",
                "newLogin@email.com",
                "duplicateNewLogin@email.com");
        mockMvc.perform(post("/admin/saveSettingLogin")
                        .requestAttr("loginDto", loginDto))
                .andDo(print()).andExpect(status().isNoContent());
    }
}