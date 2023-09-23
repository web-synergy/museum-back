package baza.trainee.controllers;

import baza.trainee.enums.TypePicture;
import baza.trainee.services.ResourcePictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The {@code ResourcePictureController} class is a Spring MVC REST controller
 * * responsible for handling get picture
 * * result return resource of picture,
 * * example: localhost:8080/picture/original/look.jpg
 * *{original default dir in file system of directory uploads/original}.
 * *
 * * @author Andry Sitarsky
 * * @version 1.0
 * * @since 2023-09-05
 */

@RestController
@RequestMapping("/picture")
@RequiredArgsConstructor
public class ResourcePictureController {
    /** Service for get picture.*/
    private final ResourcePictureService resourcePictureService;

    /**
     * Method get image in directory rootLocal/{type}.
     *
     * @param type     original or compression file
     * @param filename path in directory rootLocation/{type}
     * @return picture
     */
    @GetMapping(value = "/{type}/{*filename}",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage(@PathVariable("type") final TypePicture type,
                           @PathVariable("filename") final String filename) {
        return resourcePictureService.getPicture(type, filename);
    }
}
