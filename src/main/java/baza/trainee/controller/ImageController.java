package baza.trainee.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import baza.trainee.service.ImageService;
import lombok.RequiredArgsConstructor;

/**
 * Controller class for managing image-related HTTP requests and responses.
 * This controller provides endpoints for retrieving images and saving temporary images.
 *
 * @author Evhen Malysh
 */
@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService storageService;

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
        return storageService.loadResource(filename, type);
    }

    /**
     * Get a temporary image by filename and session.
     *
     * @param session  The HTTP session associated with the request.
     * @param filename The name of the temporary image file.
     * @return A byte array containing the temporary image data.
     */
    @GetMapping(value = "/temp", produces = MediaType.IMAGE_JPEG_VALUE)
    byte[] getTempImage(
            final HttpSession session,
            @RequestParam("filename") final String filename
    ) {
        String sessionId = session.getId();
        return storageService.loadTempResource(filename, sessionId);
    }

    /**
     * Save an image as a temporary resource in the session.
     *
     * @param session The HTTP session associated with the request.
     * @param file    The {@link MultipartFile} to be saved.
     * @return The name of the saved image file.
     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    String saveImage(
            final HttpSession session,
            @RequestParam("file") final MultipartFile file
    ) {
        String sessionId = session.getId();
        return storageService.storeToTemp(file, sessionId);
    }
}
