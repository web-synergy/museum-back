package baza.trainee.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PictureTempService {
    /**
     * Add picture in rootLocation/temp directory
     *
     * @param newFile file from form
     * @param dir rootLocation/temp/own directory in file system
     * @return Short path of rootLocation/temp directory*/
    String addPicture(MultipartFile newFile, String dir);

    /**
     * Move and Compression files in temp directory to original and
     * preview directory
     *  @param oldPathsFile List of short paths in temp directory
     * @param dir User directory when write file in rootLocation/original or
     * preview directory
     * @return paths of new file*/
    List<String> moveAndCompressionFileToFolder(List<String> oldPathsFile, String dir);

    /**
     * Delete file in folder rootLocation/original and rootLocation/preview
     *
     * @param pathsFile List short path in folder rootLocation/original and rootLocation/preview
     * */
    void deleteFilesInFolders(List<String> pathsFile);

    /**
     * Delete files without path from new list in folder rootLocation/original and rootLocation/preview
     * Leave old files from new list in folder rootLocation/original and rootLocation/preview
     * Move new file from temp directory in folder rootLocation/original and rootLocation/preview
     *
     *  @param pathsFile List short path in folder rootLocation/original and rootLocation/preview
     * @param dest Destination directory in rootLocation
     * @return paths old and new file*/

    List<String> updateFilesInFolder(List<String> pathsFile, String dest);
}
