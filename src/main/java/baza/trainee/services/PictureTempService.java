package baza.trainee.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PictureTempService {
    String addPicture(MultipartFile newFile, String dir);
    void moveToFolder(List<String> oldPathsFile, String dir);
}
