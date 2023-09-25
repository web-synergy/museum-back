package baza.trainee.controllers;

import baza.trainee.enums.TypePicture;
import baza.trainee.services.PictureTempService;
import baza.trainee.services.ResourcePictureService;
import jakarta.servlet.http.HttpSession;
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
    /**Name destination directory.*/
    private static final String NAME_DEST = "nameDest";
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
     * @param session Session
     * @return short path of file, example:/{uuid}/look.jpg
     */
    @PostMapping("/addTempFile")
    public String addPicture(final MultipartFile newFile,
                             final HttpSession session) {
        if (session.getAttribute(NAME_DEST) == null) {
            session.setAttribute(NAME_DEST, pictureService.getDir());
        }
        return pictureService.addPicture(newFile, "userId",
                (String) session.getAttribute(NAME_DEST));
    }


    /**
     * Get file in directory temp.
     *
     * @param filename path file:{/uuid/filename}
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
