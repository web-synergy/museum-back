package web.synergy.api.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import web.synergy.domain.model.User;
import web.synergy.security.RootUserInitializer;
import web.synergy.service.ArticleService;
import web.synergy.service.MuseumDataService;
import web.synergy.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RootUserInitializer rootUserInitializer;

    @MockBean
    private ArticleService articleService;

    @MockBean
    private MuseumDataService museumDataService;

    @MockBean
    private UserService userService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @SneakyThrows
    void getAll() {
        var user = new User();
        user.setEmail("email");
        user.setPassword("password");
        when(userService.getAll()).thenReturn(List.of(user));

        var result = mvc.perform(get("/api/users")).andReturn();
        var response = result.getResponse();

        assertEquals(200, response.getStatus());
        assertEquals(response.getContentAsString(), mapper.writeValueAsString(List.of(user)));
    }

    @Test
    @SneakyThrows
    void deleteUser() {
        var result = mvc.perform(delete("/api/users/{email}", "email")).andReturn();
        var response = result.getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    @SneakyThrows
    void createUser() {
        var email = "email";
        var password = "password";
        var user = new User();
        user.setEmail(email);
        user.setPassword(password);

        when(userService.createRootUser(email, password)).thenReturn(user);

        var result = mvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user)
                        )).andReturn();
        var response = result.getResponse();

        assertEquals(200, response.getStatus());
    }
}