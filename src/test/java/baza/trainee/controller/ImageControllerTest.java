package baza.trainee.controller;

import baza.trainee.service.ImageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ImageController.class)
class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageService imageService;

    @Test
    void testGetImage() throws Exception {
        var file = new File("src/test/resources/test-images/test.jpg");
        var resource = new UrlResource(file.toURI());
        byte[] imageBytes = resource.getContentAsByteArray();

        when(imageService.loadResource(anyString(), anyString())).thenReturn(imageBytes);

        mockMvc.perform(get("/api/images")
                        .param("filename", "example.jpg")
                        .param("type", "preview")
                        .contentType(MediaType.IMAGE_JPEG_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes(imageBytes));
    }

    @Test
    public void testGetTempImage() throws Exception {
        var file = new File("src/test/resources/test-images/test.jpg");
        var resource = new UrlResource(file.toURI());
        byte[] imageBytes = resource.getContentAsByteArray();

        when(imageService.loadTempResource(anyString(), anyString())).thenReturn(imageBytes);

        MockHttpSession session = new MockHttpSession(null, "session123");

        mockMvc.perform(get("/api/images/temp")
                        .session(session)
                        .param("filename", "temp.jpg")
                        .contentType(MediaType.IMAGE_JPEG_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes(imageBytes));
    }

    @Test
    public void testSaveImage() throws Exception {
        MockHttpSession session = new MockHttpSession(null, "session123");

        var file = new File("src/test/resources/test-images/test.jpg");
        var resource = new UrlResource(file.toURI());
        byte[] imageBytes = resource.getContentAsByteArray();

        MockMultipartFile mockFile = new MockMultipartFile("file", "example.jpg", "image/jpeg", imageBytes);

        when(imageService.storeToTemp(eq(mockFile), anyString())).thenReturn("example.jpg");

        mockMvc.perform(multipart("/api/images").file(mockFile).session(session))
                .andExpect(status().isCreated())
                .andExpect(content().string("example.jpg"));
    }
}
