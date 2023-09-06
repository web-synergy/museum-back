package baza.trainee.controllers;

import baza.trainee.services.ResourcePictureFromCacheOrBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 *  The {@code ResourcePictureController} class is a Spring MVC REST controller
 *  * responsible for handling get picture
 *  * result return resource of picture, example: localhost:8080/pictureCache/2023/look.jpg
 *  *{2023 default dir. We change dir on entity id}
 *  *
 *  * @author Andry Sitarsky
 *  * @version 1.0
 *  * @since 2023-09-05*/

@RestController
@RequestMapping("/pictureCache")
@RequiredArgsConstructor
public class ResourcePictureFromCacheOrBaseController {
    private ResourcePictureFromCacheOrBaseService resourcePictureService;

    @GetMapping(value = "/{*filename}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage(@PathVariable("filename") String filename) throws
            IOException {
        Resource resource = resourcePictureService.loadAsResource(filename);
        return resource.getContentAsByteArray();
    }
}
