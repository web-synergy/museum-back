package baza.trainee.utils;

import net.coobird.thumbnailator.Thumbnails;

import org.springframework.web.multipart.MultipartFile;

import baza.trainee.exceptions.custom.ImageCompressionException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageCompressor {

    /**
     * Compresses an input image file represented as a MultipartFile to reduce its dimensions and quality.
     *
     * @param inputFile   The input image file to be compressed.
     * @param targetWidth The target width for the compressed image.
     * @param quality     The quality level for the compressed image (a value between 0.0 and 1.0).
     * @return A compressed MultipartFile representing the compressed image.
     * @throws ImageCompressionException If an I/O error occurs while processing the input file.
     */
    public static MultipartFile compress(final MultipartFile inputFile, final int targetWidth, final float quality) {
        try (InputStream inputStream = inputFile.getInputStream()) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Thumbnails.of(inputStream)
                    .width(targetWidth)
                    .outputFormat("JPEG")
                    .outputQuality(quality)
                    .toOutputStream(outputStream);

            byte[] jpegData = outputStream.toByteArray();

            return new CustomMultipartFile(inputFile.getOriginalFilename(), inputFile.getName(),
                    "image/jpeg", new ByteArrayInputStream(jpegData));
        } catch (IOException e) {
            throw new ImageCompressionException(e.getMessage());
        }
    }
}
