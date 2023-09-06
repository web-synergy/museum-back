package baza.trainee.services;

import org.springframework.web.multipart.MultipartFile;

public interface PictureService {
    String addPicture(MultipartFile newPicture, String ownDir);
    String changePicture(String oldPath, MultipartFile newPicture);
    boolean deletePicture(String oldPath);
}
