package baza.trainee.services;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PictureTempService {
    /**
     * Add and compression picture in rootLocation/temp/userId/{type} directory.
     *
     * @param newPicture file from form
     * @param userId     Id user
     * @param dest       uuid directory
     * @return Short path /{uuid}/filename
     */
    String addPicture(MultipartFile newPicture, String userId,
                      String dest);

    /**
     * Move  files in temp directory to rootLocation/{type} directory.
     *
     * @param sourcePathsFile List of short paths in temp directory
     * @param userId          Id user
     * @param session         Session
     **/
    void moveFilesInTempToFolder(List<String> sourcePathsFile, String userId,
                                 HttpSession session);

    /**
     * Move files in rootLocation/{type} directory to temp directory.
     *
     * @param sourcePathsFile List of short paths in temp directory
     * @param userId          Userid
     * @param session         Session
     **/
    void moveFilesInFolderToTemp(List<String> sourcePathsFile, String userId,
                                 HttpSession session);

    /**
     * Delete directories in folder rootLocation/{type} and temp.
     *
     * @param pathDeleteDir short path directory in directory
     *                      rootLocation/{type}.
     * @param userId        User id
     * @param session       Session
     */
    void deleteDirectory(String pathDeleteDir, String userId,
                         HttpSession session);

    /**
     * Get uuid name directory.
     *
     * @return Uuid name directory
     */
    String getDir();
}
