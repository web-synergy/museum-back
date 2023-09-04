package baza.trainee.controllers;

import baza.trainee.services.ResourcePictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/picture")
@RequiredArgsConstructor
public class ResourcePictureController {

    private ResourcePictureService resourcePictureService;

    @GetMapping(value = "/{filename}", produces = MediaType.IMAGE_JPEG_VALUE)
    byte[] getImage(@PathVariable("filename") String filename) throws
            IOException {
        Resource resource = resourcePictureService.loadAsResource(filename);
        return resource.getContentAsByteArray();
    }
}
