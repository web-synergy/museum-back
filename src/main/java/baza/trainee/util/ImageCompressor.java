package baza.trainee.util;

import baza.trainee.exceptions.StorageException;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageCompressor {
    private ImageCompressor() {
        throw new StorageException("Image compressor class");
    }

    /**
     * Compresses an input image file represented as a MultipartFile to reduce its dimensions and quality.
     *
     * @param inputFile   The input image file to be compressed.
     * @param targetWidth The target width for the compressed image.
     * @param quality     The quality level for the compressed image (a value between 0.0 and 1.0).
     * @return A compressed MultipartFile representing the compressed image.
     * @throws IOException If an I/O error occurs while processing the input file.
     */
    public static MultipartFile compress(final File inputFile, final int targetWidth, final float quality)
            throws IOException {
        try (InputStream inputStream = new FileInputStream(inputFile)) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Thumbnails.of(inputStream)
                    .width(targetWidth)
                    .outputFormat("JPEG")
                    .outputQuality(quality)
                    .toOutputStream(outputStream);

            byte[] jpegData = outputStream.toByteArray();
            byte[] webpData = convertToWebp(jpegData);

            return new MockMultipartFile(inputFile.getName(),
                    inputFile.getPath(),
                    "image/webp", new ByteArrayInputStream(webpData)) {
            };
        }
    }
    private static byte[] convertToWebp(final byte[] jpegData) throws IOException {
        ByteArrayInputStream jpegInputStream = new ByteArrayInputStream(jpegData);
        BufferedImage jpegImage = ImageIO.read(jpegInputStream);

        ByteArrayOutputStream webpOutputStream = new ByteArrayOutputStream();
        ImageIO.write(jpegImage, "webp", webpOutputStream);

        return webpOutputStream.toByteArray();
    }
}
