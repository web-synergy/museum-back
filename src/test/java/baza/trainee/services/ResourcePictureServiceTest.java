package baza.trainee.services;

import baza.trainee.domain.enums.TypePicture;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.UrlResource;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ResourcePictureServiceTest {

    @Autowired
    ResourcePictureService service;
    static Path rootPath;

    @BeforeAll
    static void init() throws IOException {
        final Path pathSourceFile = Path.of(System.getProperty("user.dir"), "upload",
                "noImages.jpg").normalize().toAbsolutePath();
        rootPath = Path.of(System.getProperty("user.dir"), "test");
        Files.createDirectories(rootPath.resolve(TypePicture.ORIGINAL.name().toLowerCase())
                .normalize().toAbsolutePath());
        final Path absolutePathGuestFile =
                rootPath.resolve(TypePicture.ORIGINAL.name().toLowerCase())
                        .resolve("noImages.jpg").normalize().toAbsolutePath();
        Files.copy(pathSourceFile, absolutePathGuestFile, StandardCopyOption.REPLACE_EXISTING);
        Files.createDirectories(rootPath.resolve("temp").resolve("userId")
                .resolve(TypePicture.ORIGINAL.name().toLowerCase())
                .normalize().toAbsolutePath());
        final Path absolutePathTempFile = rootPath.resolve("temp").resolve("userId")
                .resolve(TypePicture.ORIGINAL.name().toLowerCase())
                .resolve("noImages.jpg").normalize().toAbsolutePath();
        Files.copy(pathSourceFile, absolutePathTempFile, StandardCopyOption.REPLACE_EXISTING);
    }
    @AfterAll
    static void destroy() throws IOException {
        FileSystemUtils.deleteRecursively(rootPath.normalize().toAbsolutePath());
    }

    @Test
    @SneakyThrows
    void loadAsResource() {
        byte[] content = service.getPicture(TypePicture.ORIGINAL, "noImages.jpg");
        assertTrue(content.length > 0);
    }
}