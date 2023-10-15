package baza.trainee.controller.admin;

import baza.trainee.domain.model.MuseumData;
import baza.trainee.security.RootUserInitializer;
import baza.trainee.service.ArticleService;
import baza.trainee.service.MuseumDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
public class MuseumDataAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private MuseumDataService museumDataService;

    @MockBean
    private RootUserInitializer initializer;

    @MockBean
    ArticleService articleService;

    private MuseumData museumData;

    @BeforeAll
    public void setUp() {
        museumData = new MuseumData();
        museumData.setId("1");
        museumData.setPhoneNumber("123-456-7890");
        museumData.setEmail("example@example.com");
    }

    @Test
    public void authenticatedUserWithAdminRole_shouldBeAbleToPostMuseumData() throws Exception {
        String museumDataJson = objectMapper.writeValueAsString(museumData);

        when(museumDataService.add(any(MuseumData.class))).thenReturn(museumData);

        mockMvc.perform(performPost(museumDataJson, jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.phoneNumber").value("123-456-7890"))
                .andExpect(jsonPath("$.email").value("example@example.com"));
    }

    @Test
    public void authenticatedUserWithAdminRole_shouldBeAbleToUpdateMuseumData() throws Exception {
        String museumDataJson = objectMapper.writeValueAsString(museumData);

        when(museumDataService.update(any(MuseumData.class))).thenReturn(museumData);

        mockMvc.perform(performPut(museumDataJson, jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.phoneNumber").value("123-456-7890"))
                .andExpect(jsonPath("$.email").value("example@example.com"));
    }

    @Test
    public void anonymousUser_shouldNotBeAbleToPostMuseumData() throws Exception {
        String museumDataJson = objectMapper.writeValueAsString(museumData);

        when(museumDataService.add(any(MuseumData.class))).thenReturn(museumData);

        mockMvc.perform(performPost(museumDataJson, anonymous()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void anonymousUser_shouldNotBeAbleToUpdateMuseumData() throws Exception {
        String museumDataJson = objectMapper.writeValueAsString(museumData);

        when(museumDataService.update(any(MuseumData.class))).thenReturn(museumData);

        mockMvc.perform(performPut(museumDataJson, anonymous()))
                .andExpect(status().isUnauthorized());
    }

    private <T extends RequestPostProcessor> MockHttpServletRequestBuilder performPost(
            String jsonString,
            T postProcessor) {
        return post("/api/admin/museum_data")
                .with(postProcessor)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)
                .accept(MediaType.APPLICATION_JSON);
    }

    private <T extends RequestPostProcessor> MockHttpServletRequestBuilder performPut(
            String jsonString,
            T postProcessor) {
        return put("/api/admin/museum_data")
                .with(postProcessor)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)
                .accept(MediaType.APPLICATION_JSON);
    }
}
