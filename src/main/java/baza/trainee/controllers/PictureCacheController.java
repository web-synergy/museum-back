package baza.trainee.controllers;

import baza.trainee.services.PictureCacheService;
import baza.trainee.services.PictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *  The {@code PictureCacheController} class is a Spring MVC REST controller
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
public class PictureCacheController {
    @Value("${dir}")
    public String defaultDir;
    private final PictureCacheService pictureService;


    /**
     * Add picture in  cache
     *
     * @param newFile example:<input name="newFile" type=file/>
     * @return short path of file, example:/img/2023/9/look.jpg
     * */
    @PostMapping("/addCacheFile")
    public String addPicture(MultipartFile newFile) {
        return pictureService.addPicture(newFile, defaultDir);
    }

    /**
     * Change picture in  cache
     *
     * @param newFile example:<input name="newFile" type=file/>
     * @param oldPathFile example:<input name="oldPathFile" type=text/>
     * @return short path of file, example:/img/2023/9/look.jpg
     * */

    @PostMapping("/changeCacheFile")
    public String changeFile(String oldPathFile, MultipartFile newFile) {
        return pictureService.changePicture(oldPathFile, newFile);
    }

    /**
     * Delete picture in  cache
     *
     * @param oldPathFile example:<input name="oldPathFile" type=text/>
     * */

    @PostMapping("/deleteCacheFile")
    public void deleteFile(String oldPathFile) {
        pictureService.deletePicture(oldPathFile);
    }

}
