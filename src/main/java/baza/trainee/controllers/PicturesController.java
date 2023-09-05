package baza.trainee.controllers;

import baza.trainee.dtos.RequestPicturesDto;
import baza.trainee.services.PicturesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 *  The {@code PicturesController} class is a Spring MVC REST controller
 *  * responsible for handling add, change and delete list of pictures
 *  * result of add and change (list of string of file access
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

    private final PicturesService picturesService;

    @PostMapping("/addAllPicture")
    public List<String> addAllPicture(RequestPicturesDto dto) throws
            IOException {
        return picturesService.addAllPictures(dto.getFiles());
    }

    @PostMapping("/changeAllPicture")
    public List<String> changeAllPicture(RequestPicturesDto dto) throws IOException {
        return picturesService.changeAllPictures(dto.getPictures());
    }


    @PostMapping("/deleteAllPicture")
    public List<Boolean> deleteAllPicture(RequestPicturesDto dto) throws IOException {
        return picturesService.deleteAllPictures(dto.getOldPaths());
    }
}
