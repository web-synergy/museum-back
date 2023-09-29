package baza.trainee.controller.admin;

import baza.trainee.domain.model.MuseumData;
import baza.trainee.service.MuseumDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(MuseumDataAdminController.class)
public class MuseumDataAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MuseumDataService museumDataService;

    @Autowired
    private ObjectMapper objectMapper;

    private MuseumData museumData;

    @BeforeAll
    public void setUp(){
        museumData = new MuseumData();
        museumData.setId("1");
        museumData.setPhoneNumber("123-456-7890");
        museumData.setEmail("example@example.com");
    }

    @Test
    public void testAddData() throws Exception {
        String museumDataJson = objectMapper.writeValueAsString(museumData);

        when(museumDataService.add(any(MuseumData.class))).thenReturn(museumData);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/museum_data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(museumDataJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.phoneNumber").value("123-456-7890"))
                .andExpect(jsonPath("$.email").value("example@example.com"));
    }

    @Test
    public void testUpdateData() throws Exception {
        String museumDataJson = objectMapper.writeValueAsString(museumData);

        when(museumDataService.update(any(MuseumData.class))).thenReturn(museumData);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/admin/museum_data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(museumDataJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.phoneNumber").value("123-456-7890"))
                .andExpect(jsonPath("$.email").value("example@example.com"));
    }
}
