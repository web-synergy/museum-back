package baza.trainee.utils;

import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImageCompressorTest {
    @Test
    void testCompress() throws IOException {

        MultipartFile inputFile = new CustomMultipartFile(
                "test.jpg",
                "test.jpg",
                "image/jpeg",
                getClass().getResourceAsStream("/test-images/test.jpg")
        );

        MultipartFile compressedFile = ImageCompressor.compress(inputFile, 200, 0.9f);

        assertEquals("image/jpeg", compressedFile.getContentType());
        assertEquals("test.jpg", compressedFile.getOriginalFilename());
        assertTrue(inputFile.getSize() > compressedFile.getSize());
        assertEquals(200, getImageWidth(compressedFile));
    }


    private int getImageWidth(MultipartFile imageFile) throws IOException {
        InputStream inputStream = imageFile.getInputStream();
        BufferedImage image = ImageIO.read(inputStream);
        return image.getWidth();
    }

}
