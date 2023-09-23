package baza.trainee.services;

import baza.trainee.enums.TypePicture;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PictureTempServiceTest {
    @Autowired
    PictureTempService service;

    static MockMultipartFile mockFile;

    @BeforeAll
    @SneakyThrows
    static void init(){
        Path absolutePathFile = Path.of(System.getProperty("user.dir"), "upload",
                "noImages.jpg").normalize().toAbsolutePath();
        byte[] imageBytes = new UrlResource(absolutePathFile.toUri()).getContentAsByteArray();
        mockFile = new MockMultipartFile("noImages.jpg", "noImages.jpg",
                "image/jpeg", imageBytes);

    }

    @Test
    @SneakyThrows
    void addPicture() {
        service.addPicture(mockFile, "userId", "original");
        Path absolutePathNewFile = Path.of(System.getProperty("user.dir"),"upload",
                        "temp","userId", "original",service.getDir(),"noImages.jpg")
                .normalize().toAbsolutePath();
        assertTrue(absolutePathNewFile.toFile().exists());
    }

    @Test
    void moveAndCompressionFileToFolder() {
        service.addPicture(mockFile, "userId", "original");
        String path = Path.of(service.getDir(), "noImage.jpg").toString();
        service.moveFilesInTempToFolder(List.of(path), "userId");
        Path absolutePathNewFile = Path.of(System.getProperty("user.dir"),
                        "upload","original", service.getDir(), "noImages.jpg")
                .normalize().toAbsolutePath();
        assertTrue(absolutePathNewFile.toFile().exists());
    }


    @Test
    void moveFilesInFolderToTemp() {
        service.addPicture(mockFile, "userId", "original");
        String path = Path.of(service.getDir(), "noImage.jpg").toString();
        service.moveFilesInTempToFolder(List.of(path), "userId");
        service.moveFilesInFolderToTemp(List.of(path), "userId");
        Path pathDest = Path.of(System.getProperty("user.dir"),"upload","temp",
                "userId", "original", path).normalize().toAbsolutePath();
        assertTrue(pathDest.toFile().exists());

    }

    @Test
    void deleteDirectory() throws IOException {
        String shortPath = Path.of(service.getDir(), "noImage.jpg").toString();
        Path path = Path.of(System.getProperty("user.dir"),"upload", "original",
                shortPath).normalize().toAbsolutePath();
        Files.createFile(path);
        service.deleteDirectory(service.getDir(), "userId");
        assertFalse(path.toFile().exists());
    }

    @Test
    void getDir() {
        assertNotNull(service.getDir());
    }
}