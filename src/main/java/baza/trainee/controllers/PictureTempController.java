package baza.trainee.controllers;

import baza.trainee.enums.TypePicture;
import baza.trainee.services.PictureTempService;
import baza.trainee.services.ResourcePictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * *The {@code PictureTempController} class is a Spring MVC REST controller
 * * responsible for handling add and get picture in temp directory.
 * *
 * * Files are distributed directly, example:
 * * localhost:8080/admin/picture/{type}/{uuid}/look.jpg
 * *
 * * @author Andry Sitarsky
 * * @version 1.0
 * * @since 2023-09-10
 */

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class PictureTempController {
    /**
     * Service for add picture.
     */
    private final PictureTempService pictureService;
    /**
     * Service for get picture.
     */
    private final ResourcePictureService resourcePictureService;


    /**
     * Add picture in  directory rootLocation/temp.
     *
     * @param newFile Picture from form
     * @return short path of file, example:/{uuid}/look.jpg
     */
    @PostMapping("/addTempFile")
    public String addPicture(final MultipartFile newFile) {
        String nameDest = pictureService.getDir();
        return pictureService.addPicture(newFile, "userId", nameDest);
    }


    /**
     * Get file in directory temp.
     *
     * @param filename path in directory rootLocation/temp
     * @param type     Type picture
     * @return picture
     */
    @GetMapping(value = "/picture/{type}/{*filename}",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage(@PathVariable("type") final TypePicture type,
                           @PathVariable("filename") final String filename) {
        return resourcePictureService.getPictureFromTemp("userId", type,
                filename);
    }

}
