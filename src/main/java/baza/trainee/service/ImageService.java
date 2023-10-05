package baza.trainee.service;

import org.springframework.web.multipart.MultipartFile;

import baza.trainee.dto.SaveImageResponse;

import java.util.List;

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
     * @param filename  name of the loaded image.
     * @param sessionId ID of local session.
     * @param type      type of the image.
     * @return image as an array of bytes.
     */
    byte[] loadTempResource(String filename, String sessionId, String type);

    /**
     * Store {@link MultipartFile} as File to temporary file storage
     * with random filename.
     *
     * @param file      {@link MultipartFile} to store.
     * @param sessionId ID of local session.
     * @return Response with the image name of the saved file.
     */
    SaveImageResponse storeToTemp(MultipartFile file, String sessionId);

    /**
     * Store all files by filenames from temporary file storage to
     * permanent file storage.
     *
     * @param filenames names of the persisted files.
     * @param sessionId ID of local session.
     */
    void persist(List<String> filenames, String sessionId);

}
