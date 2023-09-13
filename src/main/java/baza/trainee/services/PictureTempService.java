package baza.trainee.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PictureTempService {
    /**
     * Add picture in upload/temp directory
     *
     * @param newFile file from form
     * @param dir upload/temp/own directory in file system
     * @return Short path of /upload/temp directory*/
    String addPicture(MultipartFile newFile, String dir);

    /**
     * Move and Compression files in temp directory to original and
     * preview directory
     *  @param oldPathsFile List of short paths in temp directory
     * @param dir User directory when write file in upload/original or
     * preview directory
     * @return paths of new file*/
    List<String> moveAndCompressionFileToFolder(List<String> oldPathsFile, String dir);

    /**
     * Delete file in folder upload/original and upload/preview
     *
     * @param pathsFile List short path in folder upload/original and upload/preview
     * */
    void deleteFilesInFolders(List<String> pathsFile);

    /**
     * Delete files without path from new list in folder upload/original and upload/preview
     * Leave old files from new list in folder upload/original and upload/preview
     * Move new file from temp directory in folder upload/original and upload/preview
     *  @param pathsFile List short path in folder upload/original and upload/preview
     * @return paths old and new file*/

    List<String> updateFilesInFolder(List<String> pathsFile, String userId);
}
