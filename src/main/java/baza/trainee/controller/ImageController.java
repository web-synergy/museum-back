package baza.trainee.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import baza.trainee.service.ImageService;
import lombok.RequiredArgsConstructor;

/**
 * Controller class for managing image-related HTTP requests and responses.
 * This controller provides endpoints for retrieving images.
 *
 * @author Evhen Malysh
 */
@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    /**
     * Get an image by filename and type.
     *
     * @param filename The name of the image file.
     * @param type     The type of the image (either "preview" or "original").
     * @return A byte array containing the image data.
     */
    @GetMapping(produces = MediaType.IMAGE_JPEG_VALUE)
    byte[] getImage(
            @RequestParam("filename") final String filename,
            @RequestParam("type") final String type
    ) {
        return imageService.loadResource(filename, type);
    }

}
