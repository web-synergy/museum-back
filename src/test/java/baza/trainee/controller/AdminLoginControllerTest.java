package baza.trainee.controller;

import baza.trainee.domain.dto.LoginDto;
import baza.trainee.service.AdminLoginService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminLoginController.class)
class AdminLoginControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    AdminLoginService adminLoginService;
    @MockBean
    UserDetails userDetails;

    @SneakyThrows
    @Test
    void checkOldLogin() {
        mockMvc.perform(get("/admin/checkOldLogin")
                        .with(user("usr").roles("ADMIN"))
                        .param("oldLogin", "user@email.com")
                        .requestAttr("userDetails", userDetails))
                .andDo(print()).andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void saveSettingLogin() {
        LoginDto loginDto = new LoginDto("oldLogin",
                "newLogin", "duplicateNewLogin");
        MockHttpSession session = new MockHttpSession(null, "session123");
        mockMvc.perform(post("/admin/saveSettingLogin")
                        .with(user("usr").roles("ADMIN"))
                        .requestAttr("loginDto", loginDto)
                        .session(session))
                .andDo(print()).andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void changeLogin() {
        MockHttpSession session = new MockHttpSession(null, "session123");
        mockMvc.perform(put("/admin/changeLogin")
                        .with(user("user").roles("ADMIN"))
                        .requestAttr("code", "123456")
                        .session(session))
                .andDo(print()).andExpect(status().isOk());
    }
}