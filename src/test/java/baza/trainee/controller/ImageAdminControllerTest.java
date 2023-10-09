package baza.trainee.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.io.File;
import java.util.UUID;

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

import baza.trainee.dto.SaveImageResponse;
import baza.trainee.security.RootUserInitializer;
import baza.trainee.service.ImageService;

@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
public class ImageAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageService imageService;

    @MockBean
    private RootUserInitializer rootUserInitializer;

    @Test
    public void testGetTempImage() throws Exception {
        var file = new File("src/test/resources/test-images/test.jpg");
        var resource = new UrlResource(file.toURI());
        byte[] imageBytes = resource.getContentAsByteArray();

        when(imageService.loadTempResource(anyString(), anyString(), anyString())).thenReturn(imageBytes);

        MockHttpSession session = new MockHttpSession(null, "session123");

        mockMvc.perform(
                get("/api/images/temp")
                        .session(session)
                        .param("filename", "temp.jpg")
                        .param("type", "test")
                        .contentType(MediaType.IMAGE_JPEG_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes(imageBytes));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testSaveImage() throws Exception {
        MockHttpSession session = new MockHttpSession(null, "session123");

        var file = new File("src/test/resources/test-images/test.jpg");
        var resource = new UrlResource(file.toURI());
        byte[] imageBytes = resource.getContentAsByteArray();

        MockMultipartFile mockFile = new MockMultipartFile("file", "example.jpg", "image/jpeg", imageBytes);

        var imageId = UUID.randomUUID().toString();
        var response = new SaveImageResponse();
        response.setImageId(imageId);

        when(imageService.storeToTemp(eq(mockFile), anyString())).thenReturn(response);

        mockMvc.perform(
                multipart("/api/admin/images")
                        .file(mockFile)
                        .session(session))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.imageId", is(imageId)));
    }
}
