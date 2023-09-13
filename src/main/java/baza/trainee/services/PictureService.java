package baza.trainee.services;

import org.springframework.web.multipart.MultipartFile;

public interface PictureService {
    /**
     * Add picture in upload/own directory
     *
     * @param newPicture file from form
     * @param ownDir upload/own directory in file system
     * @return Short path of /upload directory*/
    String addPicture(MultipartFile newPicture, String ownDir);

    /**
     * Change picture in upload/own directory
     *
     * @param newPicture file from form
     * @param oldPath path in upload directory
     * @return Short path of /upload directory*/
    String changePicture(String oldPath, MultipartFile newPicture);

    /**
     * Delete picture in upload/own directory
     *
     * @param oldPath path in upload directory
     * @return Is file delete or not*/
    boolean deletePicture(String oldPath);
}
