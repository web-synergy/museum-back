package baza.trainee.controllers;

import baza.trainee.services.PictureFromCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *  The {@code PictureFromCacheController} class is a Spring MVC REST controller
 *  * responsible for handling add, change and delete picture
 *  * result of add and change (string of file access path{example: /img/[2023/9/look.jpg]})
 *  * path /img refers to the directory: absolute path to the project + /uploads
 *  * It exposes endpoints under the "/admin/[addFile,...]" base path.
 *  *
 *  * Files are distributed directly, example: localhost:8080/img/2023/9/look.jpg
 *  *
 *  * @author Andry Sitarsky
 *  * @version 1.0
 *  * @since 2023-09-10*/

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class PictureFromCacheController {
    private final PictureFromCacheService pictureService;

    /**
     * Add picture in directory /uploads
     *
     * @param shortPathPicture example:<input name="shortPathPicture" type=text>
     * /img/2023/9/look.jpg</input>
     * @return Is file add or not
     * */
    @PostMapping("/addPictureFromCache")
    public boolean addPicture(String shortPathPicture) {
        return pictureService.addPicture(shortPathPicture);
    }

    /**
     * Change picture in  directory /uploads
     *
     * @param shortPathsPicture example:<input name="oldPathFile" type=text/>
     * @return Is files add or not
     * */

    @PostMapping("/addPicturesFromCache")
    public boolean changeFile(List<String> shortPathsPicture) {
        return pictureService.addPictures(shortPathsPicture);
    }

    /**
     * Change picture in  directory /uploads
     *
     * @param shortPathPicture example:<input name="oldPathFile" type=text/>
     * @return Is file delete or not
     * */

    @PostMapping("/deletePicture")
    public boolean deleteFile(String shortPathPicture) {
        return pictureService.deletePicture(shortPathPicture);
    }

    /**
     * Change picture in  directory /uploads
     *
     * @param shortPathsPicture example:<input name="oldPathFile" type=text/>
     * @return Is file delete or not
     * */

    @PostMapping("/deletePictures")
    public boolean deleteFile(List<String> shortPathsPicture) {
        return pictureService.deletePictures(shortPathsPicture);
    }

}
