package baza.trainee.controllers;

import baza.trainee.dtos.RequestPicturesDto;
import baza.trainee.services.PicturesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *  The {@code PicturesController} class is a Spring MVC REST controller
 *  * responsible for handling add, change and delete list of pictures
 *  * result of add and change list of string of file access
 *  *
 *  * It exposes endpoints under the "/admin/[addAllFile,...]" base path.
 *  *
 *  * Files are distributed directly, example: localhost:8080/img/2023/9/look.jpg
 *  *
 *  * @author Andry Sitarsky
 *  * @version 1.0
 *  * @since 2023-09-03*/

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class PicturesController {

    @Value("${dir}")
    private String defaultDir;

    private final PicturesService picturesService;

    /**
     * Add list of pictures
     *
     * @param  dto contains list of MultipartFile
     * example of element list: <input name="pictures[0].newFile" type=file/>
     * @return list of short path file
     * */

    @PostMapping("/addAllPicture")
    public List<String> addAllPicture(RequestPicturesDto dto) {
        return picturesService.addAllPictures(dto.getFiles(), defaultDir);
    }

    /**
     * Change list of pictures
     *
     * @param  dto contains list of MultipartFile
     * example of element list: <input name="pictures[0].newFile" type=file/>
     *             <input name="pictures[0].oldPath" type=text/>
     * @return list of short path file
     * */

    @PostMapping("/changeAllPicture")
    public List<String> changeAllPicture(RequestPicturesDto dto) {
        return picturesService.changeAllPictures(dto.getPictures());
    }

    /**
     * Delete pictures
     *
     * @param  dto contains list of String of old files
     * example of element list: <input name="pictures[0].oldPath" type=text/>
     * @return Is files of list delete or not
     * */

    @PostMapping("/deleteAllPicture")
    public List<Boolean> deleteAllPicture(RequestPicturesDto dto) {
        return picturesService.deleteAllPictures(dto.getOldPaths());
    }
}
