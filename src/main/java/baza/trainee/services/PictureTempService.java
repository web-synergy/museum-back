package baza.trainee.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PictureTempService {
    /**
     * Add and compression picture in rootLocation/temp/userId/{type} directory
     *
     * @param newFile file from form
     * @param userId Id user
     * @param dir uuid directory
     * @return Short path of rootLocation/temp/userId/{type} directory*/
    String addPicture(MultipartFile newFile, String userId,
                      String dir);

    /**
     * Move  files in temp directory to rootLocation/{type} directory
     *
     *  @param sourcePathsFile List of short paths in temp directory
     * @param userId Id user
     **/
    void moveFilesInTempToFolder(List<String> sourcePathsFile, String userId);

    /**
     * Move files in rootLocation/{type} directory to temp directory
     *
     *  @param sourcePathsFile List of short paths in temp directory
     * @param userId Id user
     **/
    void moveFilesInFolderToTemp(List<String> sourcePathsFile, String userId);

    /**
     * Delete directories in folder rootLocation/{type} and temp
     *
     * @param pathsDeleteDir  short path directory in directory rootLocation/original and rootLocation/preview
     * */
    void deleteDirectory(String pathsDeleteDir, String userId);


    String getFullPath(String ...arg);

    String getDir();
}
