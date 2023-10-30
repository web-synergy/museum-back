package web.synergy.service;

import org.springframework.web.multipart.MultipartFile;

import web.synergy.dto.SaveImageResponse;

public interface ImageService {

    /**
     * Load image from file storage as array of bytes.
     *
     * @param filename name of the loaded image.
     * @param type     type of the image.
     * @return image as an array of bytes.
     */
    byte[] loadResource(String filename, String type);

    /**
     * Load image from temporary file storage as array of bytes.
     *
     * @param filename name of the loaded image.
     * @param username Admin`s username.
     * @param type     type of the image.
     * @return image as an array of bytes.
     */
    byte[] loadTempResource(String filename, String username, String type);

    /**
     * Store {@link MultipartFile} as File to temporary file storage
     * with random filename.
     *
     * @param file     {@link MultipartFile} to store.
     * @param username Admin`s username.
     * @return Response with the image name of the saved file.
     */
    SaveImageResponse storeToTemp(MultipartFile file, String username);

    /**
     * Store all files by filenames from temporary file storage to
     * permanent file storage.
     *
     * @param imageId  ID of the persisted files.
     * @param username Admin`s username.
     */
    void persist(String imageId, String username);

    /**
     * Delete existing image.
     *
     * @param imageId ID of image to delete.
     */
    void deleteImage(String imageId);

}
