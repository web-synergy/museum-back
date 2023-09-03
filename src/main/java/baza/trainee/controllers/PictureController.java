package baza.trainee.controllers;

import baza.trainee.services.PictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class PictureController {

    private final PictureService pictureService;

    @PostMapping("/addFile")
    public String addPicture(MultipartFile newFile) throws IOException {
        return pictureService.addPicture(newFile);
    }

    @PostMapping("/changeFile")
    public String changeFile(String oldPathFile, MultipartFile newFile) throws IOException {
        return pictureService.changePicture(oldPathFile, newFile);
    }

    @DeleteMapping("/deleteFile")
    public boolean deleteFile(String oldPathFile) throws IOException {
        return pictureService.deletePicture(oldPathFile);
    }
}
