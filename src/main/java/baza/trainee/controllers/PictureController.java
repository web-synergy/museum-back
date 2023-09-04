package baza.trainee.controllers;

import baza.trainee.services.PictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 *  The {@code PictureController} class is a Spring MVC REST controller
 *  * responsible for handling add, change and delete picture
 *  * result of add and change (string of file access path{example: /img/[2023/9/look.jpg]})
 *  * path /img refers to the directory: absolute path to the project + /uploads
 *  * It exposes endpoints under the "/admin/[addFile,...]" base path.
 *  *
 *  * Files are distributed directly, example: localhost:8080/img/2023/9/look.jpg
 *  *
 *  * @author Andry Sitarsky
 *  * @version 1.0
 *  * @since 2023-09-03*/

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class PictureController {

    private final PictureService pictureService;

    /**
     * Add picture in directory /uploads
     *
     * @param newFile example:<input name="newFile" type=file/>
     * @return short path of file, example:/img/2023/9/look.jpg
     * */
    @PostMapping("/addFile")
    public String addPicture(MultipartFile newFile) throws IOException {
        return pictureService.addPicture(newFile);
    }

    /**
     * Change */

    @PostMapping("/changeFile")
    public String changeFile(String oldPathFile, MultipartFile newFile) throws IOException {
        return pictureService.changePicture(oldPathFile, newFile);
    }

    @DeleteMapping("/deleteFile")
    public boolean deleteFile(String oldPathFile) throws IOException {
        return pictureService.deletePicture(oldPathFile);
    }

}
