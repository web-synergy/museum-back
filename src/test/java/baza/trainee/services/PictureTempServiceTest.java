package baza.trainee.services;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PictureTempServiceTest {
    @Autowired
    PictureTempService service;

    @BeforeEach
    void init(){

    }

    @Test
    @SneakyThrows
    void addPicture() {
        Path absolutePathFile = Path.of(System.getProperty("user.dir"), "upload",
                "noImages.jpg").normalize().toAbsolutePath();
        byte[] imageBytes = new UrlResource(absolutePathFile.toUri()).getContentAsByteArray();
        MockMultipartFile mockFile = new MockMultipartFile("file", "noImages.jpg",
                "image/jpeg", imageBytes);
        service.addPicture(mockFile, "userId", "user");
        Path absolutePathNewFile = Path.of(System.getProperty("user.dir"),
                "upload","temp","user","noImages.jpg")
                .normalize().toAbsolutePath();
        assertTrue(absolutePathNewFile.toFile().exists());
    }

    @Test
    void moveAndCompressionFileToFolder() {
        service.moveFilesInTempToFolder(List.of("noImage.jpg"), "entity");
    }


}