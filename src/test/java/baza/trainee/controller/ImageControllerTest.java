package baza.trainee.controller;

import baza.trainee.dto.SaveImageResponse;
import baza.trainee.security.RootUserInitializer;
import baza.trainee.service.ImageService;
import baza.trainee.service.impl.ImageServiceImpl.ImageType;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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

    @Test
    void testGetTempImage() throws Exception {
        var file = new File("src/test/resources/test-images/test.jpg");
        var resource = new UrlResource(file.toURI());
        byte[] imageBytes = resource.getContentAsByteArray();

        when(imageService.loadTempResource(anyString(), anyString(), anyString())).thenReturn(imageBytes);

        MockHttpSession session = new MockHttpSession(null, "session123");

        mockMvc.perform(get("/api/images/temp")
                        .session(session)
                        .param("filename", "temp.jpg")
                        .param("type", ImageType.PREVIEW.getValue())
                        .contentType(MediaType.IMAGE_JPEG_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes(imageBytes));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testSaveImage() throws Exception {
        var session = new MockHttpSession(null, "session123");

        var file = new File("src/test/resources/test-images/test.jpg");
        var resource = new UrlResource(file.toURI());
        byte[] imageBytes = resource.getContentAsByteArray();

        var mockFile = new MockMultipartFile("file", "example.jpg", "image/jpeg", imageBytes);
        var response = new SaveImageResponse();
        response.imageId(UUID.randomUUID().toString());
       
        when(imageService.storeToTemp(eq(mockFile), anyString())).thenReturn(response);

        mockMvc.perform(multipart("/api/admin/images").file(mockFile).session(session))
                .andExpect(status().isCreated());
    }
}
