package web.synergy.controller;

import web.synergy.security.RootUserInitializer;
import web.synergy.service.ArticleService;
import web.synergy.service.ImageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import web.synergy.service.MuseumDataService;

import java.io.File;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageService imageService;

    @MockBean
    private RootUserInitializer rootUserInitializer;

    @MockBean
    private ArticleService articleService;

    @MockBean
    private MuseumDataService museumDataService;

    @Test
    void testGetImage() throws Exception {
        var file = new File("src/test/resources/test-images/test.jpg");
        var resource = new UrlResource(file.toURI());
        byte[] imageBytes = resource.getContentAsByteArray();

        when(imageService.loadResource(anyString(), anyString())).thenReturn(imageBytes);

        mockMvc.perform(
                get("/api/images")
                        .param("filename", "example.jpg")
                        .param("type", "preview")
                        .contentType(MediaType.IMAGE_JPEG_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes(imageBytes));
    }
}
