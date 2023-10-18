package baza.trainee.utils;

import baza.trainee.exceptions.custom.StorageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileSystemStorageUtilsTest {

    File testImage;

    @BeforeEach
    void init() {
        testImage = new File("src/test/resources/test-images/test.jpg");
    }

    @Test
    void loadFilePathTest() {

        // given:
        var expected = testImage.toPath();
        System.out.println(expected);
        var parent = Paths.get(testImage.getParent());
        System.out.println(parent);

        // when:
        var actual = FileSystemStorageUtils.loadFilePath(parent);
        System.out.println(actual);

        // then:
        assertEquals(expected, actual);
    }

    @Test
    void loadPathTestFailsIfFileIsNotExists(@TempDir Path path) {

        // expected:
        assertThrows(StorageException.class, () -> FileSystemStorageUtils.loadFilePath(path),
                "Failed to find files");
    }

    @Test
    void storeToPathTest(@TempDir Path temp) throws IOException {

        // given:
        var file = new MockMultipartFile(testImage.getName(), new FileInputStream(testImage));

        // when:
        FileSystemStorageUtils.storeToPath(file, temp);
        Path savedFilePath = temp.resolve(file.getName());
        UrlResource resource = new UrlResource(savedFilePath.toUri());

        // then:
        assertTrue(resource.isReadable());
        assertTrue(resource.isFile());
        assertTrue(resource.exists());
    }

    @Test
    void storeToPathFailsIfFileIsEmptyTest(@TempDir Path temp) {

        // given:
        var file = new MockMultipartFile("emptyFile.txt", new byte[0]);

        // then:
        assertThrows(StorageException.class, () -> FileSystemStorageUtils.storeToPath(file, temp),
                "Failed to store empty file.");
    }

    @Test
    void initTest(@TempDir Path temp) {

        // given:
        var path = temp.resolve("initDir");

        // when:
        FileSystemStorageUtils.init(path);
        var createdDir = new File(path.toUri());

        // then:
        assertTrue(createdDir.isDirectory());
        assertTrue(createdDir.canRead());
    }

}
