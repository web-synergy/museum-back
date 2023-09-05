package baza.trainee.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PictureService {
    String addPicture(MultipartFile newPicture, String ownDir) throws IOException;
    String changePicture(String oldPath, MultipartFile newPicture)
            throws IOException;
    boolean deletePicture(String oldPath) throws IOException;
}
