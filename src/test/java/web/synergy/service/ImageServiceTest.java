package web.synergy.service;

import web.synergy.config.ImageCompressionConfig;
import web.synergy.config.StorageProperties;
import web.synergy.service.impl.ImageServiceImpl;
import web.synergy.service.impl.ImageServiceImpl.ImageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImageServiceTest {

    private StorageProperties storageProperties = new StorageProperties();
    private ImageCompressionConfig imageProperties = new ImageCompressionConfig();

    @TempDir
    private Path rootImageLocation;
    private File testImage;

    private String imagesDir;
    private String tempDir;
    private String desktopDir;
    private String previewDir;

    private ImageService imageService;

    @BeforeEach
    void init() {
        storageProperties.setRootImageLocation(rootImageLocation.toString());
        desktopDir = storageProperties.getOriginalImagesLocation();
        previewDir = storageProperties.getCompressedImagesLocation();
        imagesDir = storageProperties.getPersistImageLocation();
        tempDir = storageProperties.getTempImagesLocation();

        imageService = new ImageServiceImpl(storageProperties, imageProperties);

        testImage = new File("src/test/resources/test-images/test.jpg");
    }

    @Test
    void initializationTest() {

        // given:
        Path tempPath = rootImageLocation.resolve(tempDir);

        File originalDirectory = new File(rootImageLocation.toUri());
        File tempDirectory = new File(tempPath.toUri());

        // expected:
        assertTrue(Files.isDirectory(rootImageLocation));
        assertTrue(Files.isDirectory(tempPath));
        assertTrue(originalDirectory.exists());
        assertTrue(tempDirectory.exists());
    }

    @Test
    void storeToTempTest() throws IOException {

        // given:
        var filename = testImage.getName();
        var file = new MockMultipartFile(
                filename,
                filename,
                "image/jpeg",
                new FileInputStream(testImage));

        var sessionId = "fakeSessionId";

        var sessionDir = rootImageLocation
                .resolve(tempDir)
                .resolve(sessionId);

        // when:
        var generatedFileName = imageService.storeToTemp(file, sessionId);

        // then:
        assertFalse(generatedFileName.getImageId().isBlank());

        // when:
        var createdDesktopFile = sessionDir
                .resolve(generatedFileName.getImageId())
                .resolve(desktopDir)
                .toFile();
        var createdPreviewFile = sessionDir
                .resolve(generatedFileName.getImageId())
                .resolve(previewDir)
                .toFile();

        // then:
        assertTrue(createdDesktopFile.exists());
        assertTrue(createdPreviewFile.exists());
    }

    @Test
    void persistTest() throws IOException {

        // given:
        String filename = testImage.getName();

        var file = new MockMultipartFile(
                filename,
                filename,
                "image/jpeg",
                new FileInputStream(testImage));
        var sessionId = "fakeSessionId";

        var generatedFileId = imageService.storeToTemp(file, sessionId);

        // Path => /root/images/image-UUID/desktop/file.webp
        var createdDesktopFile = rootImageLocation
                .resolve(imagesDir)
                .resolve(generatedFileId.getImageId())
                .resolve(desktopDir)
                .toFile();

        // Path => /root/images/image-UUID/preview/file.webp
        var createdPreviewFile = rootImageLocation
                .resolve(imagesDir)
                .resolve(generatedFileId.getImageId())
                .resolve(previewDir)
                .toFile();

        // when:
        imageService.persist(generatedFileId.getImageId(), sessionId);

        // then:
        assertTrue(createdDesktopFile.exists());
        assertTrue(createdPreviewFile.exists());
    }

    @Test
    void loadTempResourceTest() throws IOException {

        // given:
        String filename = testImage.getName();
        var file = new MockMultipartFile(
                filename,
                filename,
                "image/jpeg",
                new FileInputStream(testImage));
        var sessionId = "fakeSessionId";

        // when:
        String type = ImageType.PREVIEW.getValue();
        var generatedFileName = imageService.storeToTemp(file, sessionId);
        byte[] tempResource = imageService.loadTempResource(generatedFileName.getImageId(), sessionId, type);

        // then:
        assertTrue(tempResource.length > 0);
    }

    @Test
    void loadResourceTest() throws IOException {

        // given:
        String filename = testImage.getName();
        var file = new MockMultipartFile(
                filename,
                filename,
                "image/jpeg",
                new FileInputStream(testImage));
        var sessionId = "fakeSessionId";

        // when:
        var generatedFileName = imageService.storeToTemp(file, sessionId);
        
        String imageId = generatedFileName.getImageId();
        imageService.persist(imageId, sessionId);

        byte[] previewsResource = imageService.loadResource(imageId, ImageType.PREVIEW.getValue());
        byte[] originalsResource = imageService.loadResource(imageId, ImageType.ORIGINAL.getValue());

        // then:
        assertTrue(previewsResource.length > 0);
        assertTrue(originalsResource.length > 0);
    }

}