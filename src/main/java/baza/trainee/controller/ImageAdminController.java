package baza.trainee.controller;

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
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/**
 * Controller class for managing image-related HTTP requests and responses.
 * This controller provides endpoints for retrieving and saving temporary
 * images.
 *
 * @author Evhen Malysh
 */
@RestController
@RequestMapping("/api/admin/images")
@RequiredArgsConstructor
public class ImageAdminController {

    private final ImageService imageService;

    /**
     * Get a temporary image by filename and session.
     *
     * @param session  The HTTP session associated with the request.
     * @param filename The name of the temporary image file.
     * @param type     The type of the image (either "preview" or "original").
     * @return A byte array containing the temporary image data.
     */
    @GetMapping(value = "/temp", produces = MediaType.IMAGE_JPEG_VALUE)
    byte[] getTempImage(
            final HttpSession session,
            @RequestParam("filename") final String filename,
            @RequestParam("type") final String type
    ) {
        String sessionId = session.getId();
        return imageService.loadTempResource(filename, sessionId, type);
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
        return imageService.storeToTemp(file, sessionId);
    }

}
