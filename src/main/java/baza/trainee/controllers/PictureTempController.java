package baza.trainee.controllers;

import baza.trainee.services.PictureTempService;
import baza.trainee.services.ResourcePictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
    @Value("${dir.temp}")
    public String defaultDir;

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
        return pictureService.addPicture(newFile, defaultDir);
    }


    /**
     * Move and compression files in directory temp to directory rootLocation
     *
     * @param oldPathsFile file in directory temp
     * */
    @PostMapping("/moveToFolder")
    public void moveAndCompressionToFolder(List<String> oldPathsFile) {
        pictureService.moveAndCompressionFileToFolder(oldPathsFile, defaultDir);
    }

    /**
     * Delete directory in directory rootLocation
     *
     * @param dir short path directory in directory rootLocation
     * */
    @DeleteMapping("/deleteDirectory")
    public void deleteFolder(String dir){
        pictureService.deleteDirectory(dir);
    }

    /**
     * Update file in directory rootLocation
     * Delete file without path
     * Leave old file and add files in directory temp
     *
     * @param pathsFile file in directory rootLocation
     * */
    @PostMapping("/updateFileInFolder")
    public void updateFilesInFolder(List<String> pathsFile){
        pictureService.updateFilesInFolder(pathsFile,"userId");
    }

    /**
     * Get file in directory temp
     *
     * @param filename path in directory rootLocation/temp
     * */

    @GetMapping(value = "/picture/{*filename}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage(@PathVariable("filename") String filename)
            throws IOException {
        return resourcePictureService.loadAsResource(defaultDir, filename).getContentAsByteArray();
    }

}
