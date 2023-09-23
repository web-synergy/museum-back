package baza.trainee.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PictureTempService {
    /**
     * Add and compression picture in rootLocation/temp/userId/{type} directory.
     *
     * @param newPicture file from form
     * @param userId Id user
     * @param dest uuid directory
     * @return Short path /{uuid}/filename*/
    String addPicture(MultipartFile newPicture, String userId,
                      String dest);

    /**
     * Move  files in temp directory to rootLocation/{type} directory.
     *
     *  @param sourcePathsFile List of short paths in temp directory
     * @param userId Id user
     **/
    void moveFilesInTempToFolder(List<String> sourcePathsFile, String userId);

    /**
     * Move files in rootLocation/{type} directory to temp directory.
     *
     *  @param sourcePathsFile List of short paths in temp directory
     * @param userId Userid
     **/
    void moveFilesInFolderToTemp(List<String> sourcePathsFile, String userId);

    /**
     * Delete directories in folder rootLocation/{type} and temp.
     *
     * @param pathDeleteDir short path directory in directory
     *                       rootLocation/{type}.
     * @param userId User id
     * */
    void deleteDirectory(String pathDeleteDir, String userId);

    /**
     * Get full path from massive of string.
     *
     * @return Uuid name directory*/
    String getDir();
}
