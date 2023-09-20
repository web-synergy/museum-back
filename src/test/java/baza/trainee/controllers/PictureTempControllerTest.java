package baza.trainee.controllers;

import baza.trainee.services.PictureTempService;
import baza.trainee.services.ResourcePictureService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Path;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PictureTempController.class)
class PictureTempControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    PictureTempService pictureTempService;

    @MockBean
    ResourcePictureService resourcePictureService;

    @Test
    @SneakyThrows
    void addPicture() {
        Path absolutePathFile = Path.of(System.getProperty("user.dir"), "upload",
                "noImages.jpg").normalize().toAbsolutePath();
        byte[] imageBytes = new UrlResource(absolutePathFile.toUri()).getContentAsByteArray();
        MockMultipartFile mockFile = new MockMultipartFile("file", "noImages.jpg",
                "image/jpeg", imageBytes);
        when(pictureTempService.addPicture(eq(mockFile), "userId", anyString())).thenReturn("noImages.jpg");
        mockMvc.perform(multipart("/admin/addTempFile").file(mockFile))
                .andExpect(status().isOk());
    }



    @Test
    @SneakyThrows
    void getImage() {
        byte[] img = new UrlResource(Path.of(System.getProperty("user.dir"),
                "upload", "noImages.jpg").normalize().toAbsolutePath().toUri())
                .getContentAsByteArray();
        when(resourcePictureService.loadAsResource(anyString(), anyString())).thenReturn(img);
        mockMvc.perform(get("/admin/picture/{*file}" , "noImages.jpg")
                        .contentType(MediaType.IMAGE_JPEG)
                        .content(img))
                .andDo(print()).andExpect(status().isOk());
    }
}