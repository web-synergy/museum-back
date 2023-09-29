package baza.trainee.services;

import baza.trainee.domain.enums.TypePicture;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class PictureTempServiceTest {
    @Autowired
    PictureTempService service;

    static MockMultipartFile mockFile;
    static Path absolutePathFile;

    @BeforeAll
    @SneakyThrows
    static void init() {
        absolutePathFile = Path.of(System.getProperty("user.dir"), "upload",
                "noImages.jpg").normalize().toAbsolutePath();
        byte[] imageBytes = new UrlResource(
                absolutePathFile.toUri()).getContentAsByteArray();
        mockFile = new MockMultipartFile("noImages.jpg", "noImages.jpg",
                "image/jpeg", imageBytes);

    }

    @AfterEach
    void destroy() throws IOException {
        Path rootPath = Path.of(System.getProperty("user.dir"), "test")
                .normalize().toAbsolutePath();
        FileSystemUtils.deleteRecursively(
                rootPath.normalize().toAbsolutePath());
    }

    @Test
    @SneakyThrows
    void addPicture() {
        service.addPicture(mockFile, "userId", "uuid");
        Path absolutePathNewFile =
                Path.of(System.getProperty("user.dir"), "test",
                                "temp", "userId", "original", "uuid",
                                "noImages.jpg")
                        .normalize().toAbsolutePath();
        assertTrue(absolutePathNewFile.toFile().exists());
    }

    @Test
    void moveAndCompressionFileToFolder() throws IOException {
        MockHttpSession session = new MockHttpSession(null, "session123");
        for (TypePicture type : TypePicture.values()) {
            Path sourceDir =
                    Path.of(System.getProperty("user.dir"), "test", "temp",
                                    "userId", type.name().toLowerCase(), "uuid")
                            .normalize().toAbsolutePath();
            Files.createDirectories(sourceDir);
            Files.copy(absolutePathFile, sourceDir.resolve("noImages"
                    .concat(type == TypePicture.ORIGINAL ? ".jpg" : ".webp")));
        }

        String path = Path.of("uuid", "noImages.jpg").toString();

        service.moveFilesInTempToFolder(List.of(path), "userId", session);

        for (TypePicture type : TypePicture.values()) {
            Path absolutePathNewFile = Path.of(System.getProperty("user.dir"),
                            "test", type.name().toLowerCase(), "uuid",
                            "noImages".concat(type == TypePicture.ORIGINAL ? ".jpg"
                                    : ".webp"))
                    .normalize().toAbsolutePath();
            assertTrue(absolutePathNewFile.toFile().exists());
        }

    }


    @Test
    void moveFilesInFolderToTemp() throws IOException {
        MockHttpSession session = new MockHttpSession(null, "session123");
        for (TypePicture type : TypePicture.values()) {
            Path sourceDir = Path.of(System.getProperty("user.dir"), "test",
                            type.name().toLowerCase(), "uuid")
                    .normalize().toAbsolutePath();
            Files.createDirectories(sourceDir);
            Files.copy(absolutePathFile, sourceDir.resolve("noImages"
                    .concat(type == TypePicture.ORIGINAL ? ".jpg" : ".webp")));
        }

        String path = Path.of("uuid", "noImages.jpg").toString();

        service.moveFilesInFolderToTemp(List.of(path), "userId", session);

        for (TypePicture type : TypePicture.values()) {
            Path absolutePathNewFile = Path.of(System.getProperty("user.dir"),
                            "test", "temp", "userId",
                            type.name().toLowerCase(), "uuid",
                            "noImages".concat(type == TypePicture.ORIGINAL ? ".jpg"
                                    : ".webp"))
                    .normalize().toAbsolutePath();
            assertTrue(absolutePathNewFile.toFile().exists());
        }
        assertEquals("uuid", session.getAttribute("nameDest"));

    }

    @Test
    void deleteDirectory() throws IOException {
        MockHttpSession session = new MockHttpSession(null, "session123");
        for (TypePicture type : TypePicture.values()) {
            Path sourceDir = Path.of(System.getProperty("user.dir"), "test",
                            type.name().toLowerCase(), "uuid")
                    .normalize().toAbsolutePath();
            Files.createDirectories(sourceDir);
            Path sourceTempDir =
                    Path.of(System.getProperty("user.dir"), "test", "temp",
                                    "userId", type.name().toLowerCase(), "uuid")
                            .normalize().toAbsolutePath();
            Files.createDirectories(sourceTempDir);
        }
        service.deleteDirectory("uuid", "userId", session);
        for (TypePicture type : TypePicture.values()) {
            Path sourceDir = Path.of(System.getProperty("user.dir"), "test",
                            type.name().toLowerCase(), "uuid")
                    .normalize().toAbsolutePath();
            assertFalse(sourceDir.toFile().exists());
            Path sourceTempDir =
                    Path.of(System.getProperty("user.dir"), "test", "temp",
                                    "userId", type.name().toLowerCase(), "uuid")
                            .normalize().toAbsolutePath();
            assertFalse(sourceTempDir.toFile().exists());
        }

    }

    @Test
    void getDir() {
        assertNotNull(service.getDir());
    }
}