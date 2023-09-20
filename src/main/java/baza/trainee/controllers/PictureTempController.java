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
 *  The {@code PictureTempController} class is a Spring MVC REST controller
 *  * responsible for handling add, change and delete picture in directory /upload/temp
 *  * result of add and change (string of file access path{example: /pictureCache/[2023/9/look.jpg]})
 *  * path /pictureCache refers to the directory: absolute path to the project + /uploads
 *  * It exposes endpoints under the "/admin/[addFile,...]" base path.
 *  *
 *  * Files are distributed directly, example: localhost:8080/pictureCache/2023/9/look.jpg
 *  *
 *  * @author Andry Sitarsky
 *  * @version 1.0
 *  * @since 2023-09-10*/

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class PictureTempController {

    private final PictureTempService pictureService;
    private final ResourcePictureService resourcePictureService;


    /**
     * Add picture in  directory rootLocation/temp
     *
     * @param newFile Picture from form
     * @return short path of file, example:/img/2023/9/look.jpg
     * */
    @PostMapping("/addTempFile")
    public String addPicture(MultipartFile newFile) {
        String nameDest = pictureService.getDir();
        return pictureService.addPicture(newFile, "userId", nameDest);
    }


    /**
     * Get file in directory temp
     *
     * @param filename path in directory rootLocation/temp
     * */

    @GetMapping(value = "/picture/{type}/{*filename}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage(@PathVariable("type") TypePicture type,
                           @PathVariable("filename") String filename){
        String pathOfType = pictureService.getFullPath("userId", type.name().toLowerCase());
        return resourcePictureService.loadAsResource(pathOfType, filename);
    }

}
