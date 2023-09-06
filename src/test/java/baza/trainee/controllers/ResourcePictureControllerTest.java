package baza.trainee.controllers;

import baza.trainee.exceptions.StorageFileNotFoundException;
import baza.trainee.services.ResourcePictureService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static baza.trainee.constants.PictureModelConstants.VALID_IMG;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ResourcePictureController.class)
class ResourcePictureControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    ResourcePictureService resourcePictureService;


    Resource img;

    @BeforeEach
    void init(){
        try {
            img = new UrlResource(Path.of(System.getProperty("user.dir"),
                    "upload", "noImages.jpg").normalize().toAbsolutePath().toUri());
        } catch (MalformedURLException e) {
            img=null;
        }
    }

    @Test
    @SneakyThrows
    void getImage() {
        final Path url = Path.of("/picture/noImages.jpg");
        when(resourcePictureService.loadAsResource(url.getFileName().toString()))
                .thenReturn(img);
        mockMvc.perform(get("/" + url,url.getFileName().toString()))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void getImage_notStorageException(){
        final Path url = Path.of("/picture/life.jpg");
        when(resourcePictureService.loadAsResource(url.getFileName().toString()))
                .thenThrow(new StorageFileNotFoundException("Could not read file: "
                        + url.getFileName()) );
        mockMvc.perform(get("/" + url, url.getFileName().toString()))
                .andDo(print()).andExpect(status().isNotFound());
    }
}