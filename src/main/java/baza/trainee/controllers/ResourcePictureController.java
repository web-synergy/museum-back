package baza.trainee.controllers;

import baza.trainee.services.ResourcePictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 *  The {@code ResourcePictureController} class is a Spring MVC REST controller
 *  * responsible for handling get picture
 *  * result return resource of picture, example: localhost:8080/picture/original/look.jpg
 *  *{original default dir in file system of directory uploads/original}
 *  *
 *  * @author Andry Sitarsky
 *  * @version 1.0
 *  * @since 2023-09-05*/

@RestController
@RequestMapping("/picture")
@RequiredArgsConstructor
public class ResourcePictureController {
    @Value("$dir.preview")
    String preview ;
    @Value("$dir.original")
    final String original;

    private final ResourcePictureService resourcePictureService;

    /**
     * Method get image in directory upload/preview or upload/original
     *
     * @param type default original or preview
     * @param filename path in directory upload/preview or upload/original
     * @return image*/
    @GetMapping(value = "/{type}/{*filename}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage(@PathVariable("type") String type, @PathVariable("filename") String filename)
            throws IOException {
        return resourcePictureService.loadAsResource(
                type.equals(preview)? preview : original, filename).getContentAsByteArray();
    }
}
