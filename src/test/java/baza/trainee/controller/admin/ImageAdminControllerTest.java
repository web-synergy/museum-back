package baza.trainee.controller.admin;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import baza.trainee.dto.SaveImageResponse;
import baza.trainee.security.RootUserInitializer;
import baza.trainee.service.ImageService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;

@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
public class ImageAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageService imageService;

    @MockBean
    private RootUserInitializer rootUserInitializer;

    private MockMultipartFile mockFile;
    private MockHttpSession session;
    private SaveImageResponse response;

    @BeforeEach
    void setUp() throws IOException {
        File file = new File("src/test/resources/test-images/test.jpg");
        UrlResource resource = new UrlResource(file.toURI());
        byte[] imageBytes = resource.getContentAsByteArray();
        mockFile = new MockMultipartFile("file", "example.jpg", "image/jpeg", imageBytes);
        
        session = new MockHttpSession(null, "session123");

        var imageId = UUID.randomUUID().toString();
        response = new SaveImageResponse();
        response.setImageId(imageId);
    }

    @Test
    public void authorizeAdminShouldBeAbleToSaveImageToTemp() throws Exception {
        // when:
        when(imageService.storeToTemp(eq(mockFile), anyString())).thenReturn(response);

        // then:
        mockMvc.perform(performSave(session, mockFile, jwt().authorities(new SimpleGrantedAuthority("SCOPE_WRITE"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.imageId", is(response.getImageId())));
    }

    @Test
    public void anonymousUserShouldNotBeAbleToSaveImageToTemp() throws Exception {
        // when:
        when(imageService.storeToTemp(eq(mockFile), anyString())).thenReturn(response);

        // then:
        mockMvc.perform(performSave(session, mockFile, anonymous()))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.imageId").doesNotExist());
    }

    private<T extends RequestPostProcessor> MockHttpServletRequestBuilder performSave(
        MockHttpSession session,
        MockMultipartFile mockFile,
        T postProcessor) {
        return multipart("/api/admin/images")
                .file(mockFile)
                .with(postProcessor)
                .session(session);
    }
}
