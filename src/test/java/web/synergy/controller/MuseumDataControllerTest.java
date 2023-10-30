package web.synergy.controller;

import web.synergy.domain.model.MuseumData;
import web.synergy.exceptions.custom.EntityNotFoundException;
import web.synergy.security.RootUserInitializer;
import web.synergy.service.ArticleService;
import web.synergy.service.MuseumDataService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
public class MuseumDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
    public void testGetAllData() throws Exception {
        when(museumDataService.getData()).thenReturn(museumData);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/museum_data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.phoneNumber").value("123-456-7890"))
                .andExpect(jsonPath("$.email").value("example@example.com"));
    }

    @Test
    public void testGetAllDataNoData() throws Exception {
        String expectedErrMsg = "MuseumData with `no details` was not found!";

        when(museumDataService.getData()).thenThrow((new EntityNotFoundException("MuseumData", "no details")));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/museum_data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(expectedErrMsg)))
                .andExpect(status().isNotFound());
    }

}