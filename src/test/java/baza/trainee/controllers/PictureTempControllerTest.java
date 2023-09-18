package baza.trainee.controllers;

import baza.trainee.services.PictureTempService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PictureTempController.class)
class PictureTempControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    PictureTempService pictureTempService;

    @Test
    @SneakyThrows
    void addPicture() {
        Path absolutePathFile = Path.of(System.getProperty("user.dir"), "upload",
                "noImages.jpg").normalize().toAbsolutePath();
        byte[] imageBytes = new UrlResource(absolutePathFile.toUri()).getContentAsByteArray();
        MockMultipartFile mockFile = new MockMultipartFile("file", "noImages.jpg",
                "image/jpeg", imageBytes);
        when(pictureTempService.addPicture(mockFile,anyString())).thenReturn("noImages.jpg");
        mockMvc.perform(multipart("/admin/addTempFile").file(mockFile))
                .andExpect(status().isCreated())
                .andExpect(content().string("noImages.jpg"));
    }


    @Test
    @SneakyThrows
    void moveAndCompressionToFolder() {
        List<String> list = List.of("noImage.jpg");
        when(pictureTempService.moveAndCompressionFileToFolder(anyList(), anyString())).thenReturn(list);
        mockMvc.perform(post("/admin/moveToFolder")
                .requestAttr("oldPathsFile", list)).andExpect(status().isOk());
    }


}