package baza.trainee.services;

import org.springframework.web.multipart.MultipartFile;

public interface PictureCacheService {
    String addPicture(MultipartFile newFile, String dir);

    String changePicture(String oldPathFile, MultipartFile newFile);

    void deletePicture(String oldPathFile);
}
