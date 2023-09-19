package baza.trainee.service;

import baza.trainee.config.StorageProperties;
import baza.trainee.service.impl.ImageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImageServiceTest {
    private static final String ORIGINAL_IMAGES_LOCATION = "original";
    private static final String COMPRESSED_IMAGES_LOCATION = "compressed";
    private static final String TEMP_IMAGES_LOCATION = "temp";

    @TempDir
    private Path rootImageLocation;
    private File testImage;

    private ImageService imageService;

    @BeforeEach
    void init() {
        var properties = new StorageProperties();
        properties.setRootImageLocation(rootImageLocation.toString());
        properties.setOriginalImagesLocation(ORIGINAL_IMAGES_LOCATION);
        properties.setCompressedImagesLocation(COMPRESSED_IMAGES_LOCATION);
        properties.setTempImagesLocation(TEMP_IMAGES_LOCATION);

        imageService = new ImageServiceImpl(properties);

        testImage = new File("src/test/resources/test-images/test.jpg");
    }

    @Test
    void initializationTest() {

        // given:
        Path originalPath = rootImageLocation.resolve(ORIGINAL_IMAGES_LOCATION);
        Path compressedPath = rootImageLocation.resolve(ORIGINAL_IMAGES_LOCATION);
        Path tempPath = rootImageLocation.resolve(ORIGINAL_IMAGES_LOCATION);

        File originalDirectory = new File(originalPath.toUri());
        File compressedDirectory = new File(originalPath.toUri());
        File tempDirectory = new File(originalPath.toUri());

        // expected:
        assertTrue(Files.isDirectory(originalPath));
        assertTrue(Files.isDirectory(compressedPath));
        assertTrue(Files.isDirectory(tempPath));
        assertTrue(originalDirectory.exists());
        assertTrue(compressedDirectory.exists());
        assertTrue(tempDirectory.exists());
    }

    @Test
    void storeToTempTest() throws IOException {

        // given:
        var file = new MockMultipartFile(testImage.getName(), new FileInputStream(testImage));
        var sessionId = "fakeSessionId";
        var destinationDirectory = rootImageLocation.resolve(TEMP_IMAGES_LOCATION).resolve(sessionId);

        // when:
        String generatedFileName = imageService.storeToTemp(file, sessionId);

        // then:
        assertFalse(generatedFileName.isBlank());

        // when:
        File createdFile = destinationDirectory.resolve(generatedFileName).toFile();

        // then:
        assertTrue(createdFile.exists());
    }

    @Test
    void persistTest() throws IOException {

        // given:
        var file = new MockMultipartFile(testImage.getName(), new FileInputStream(testImage));
        var sessionId = "fakeSessionId";
        var originalDirectory = rootImageLocation.resolve(ORIGINAL_IMAGES_LOCATION);
        var compressedDirectory = rootImageLocation.resolve(COMPRESSED_IMAGES_LOCATION);


        // when:
        String generatedFileName = imageService.storeToTemp(file, sessionId);
        imageService.persist(List.of(generatedFileName), sessionId);

        var createdOriginalFile = originalDirectory.resolve(generatedFileName).toFile();
        var createdCompressedFile = compressedDirectory
                .resolve(generatedFileName.replaceFirst("\\..+$", ".webp"))
                .toFile();

        // then:
        assertTrue(createdOriginalFile.exists());
        assertTrue(createdCompressedFile.exists());
    }

    @Test
    void loadTempResourceTest() throws IOException {

        // given:
        var file = new MockMultipartFile(testImage.getName(), new FileInputStream(testImage));
        var sessionId = "fakeSessionId";

        // when:
        String generatedFileName = imageService.storeToTemp(file, sessionId);
        byte[] tempResource = imageService.loadTempResource(generatedFileName, sessionId);

        // then:
        assertTrue(tempResource.length > 0);
    }

    @Test
    void loadResourceTest() throws IOException {

        // given:
        var file = new MockMultipartFile(
                testImage.getName().replaceFirst("\\..+$", ".webp"),
                new FileInputStream(testImage));
        var sessionId = "fakeSessionId";

        // when:
        String generatedFileName = imageService.storeToTemp(file, sessionId);
        imageService.persist(List.of(generatedFileName), sessionId);

        byte[] previewsResource = imageService.loadResource(generatedFileName, "preview");
        byte[] originalsResource = imageService.loadResource(generatedFileName, "original");

        // then:
        assertTrue(previewsResource.length > 0);
        assertTrue(originalsResource.length > 0);
    }

}