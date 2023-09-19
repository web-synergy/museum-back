package baza.trainee.service.impl;

import baza.trainee.config.StorageProperties;
import baza.trainee.exceptions.custom.StorageException;
import baza.trainee.exceptions.custom.StorageFileNotFoundException;
import baza.trainee.service.ImageService;
import baza.trainee.utils.CustomMultipartFile;
import baza.trainee.utils.FileSystemStorageUtils;
import baza.trainee.utils.ImageCompressor;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link ImageService} interface for managing image-related operations.
 * This service handles loading, storing, and processing images.
 *
 * @author Evhen Malysh
 */
@Service
public class ImageServiceImpl implements ImageService {

    private static final int TARGET_WIDTH = 680;
    private static final float QUALITY = 0.5F;

    private final Path rootLocation;
    private final Path originalLocation;
    private final Path previewLocation;
    private final Path tempLocation;

    /**
     * Constructs an instance of the ImageServiceImpl class.
     *
     * @param storageProperties Image storage configuration properties.
     */
    public ImageServiceImpl(final StorageProperties storageProperties) {
        this.rootLocation = Paths.get(storageProperties.getRootImageLocation());
        this.originalLocation = rootLocation.resolve(storageProperties.getOriginalImagesLocation()).normalize();
        this.previewLocation = rootLocation.resolve(storageProperties.getCompressedImagesLocation()).normalize();
        this.tempLocation = rootLocation.resolve(storageProperties.getTempImagesLocation()).normalize();

        FileSystemStorageUtils.init(rootLocation, originalLocation, previewLocation, tempLocation);
    }

    /**
     * Load a resource (image) by filename and type.
     *
     * @param filename The name of the file.
     * @param type     The type of the resource (either "preview" or "original").
     * @return A byte array containing the resource data.
     * @throws StorageFileNotFoundException If the resource file cannot be found or read.
     */
    @Override
    public byte[] loadResource(final String filename, final String type) {
        var currentPath = type.equals("preview") ? previewLocation : originalLocation;
        return getResourceFromPath(filename, currentPath);
    }

    /**
     * Load a temporary resource (image) by filename and session ID.
     *
     * @param filename  The name of the file.
     * @param sessionId The ID of the session associated with the temporary resource.
     * @return A byte array containing the temporary resource data.
     * @throws StorageFileNotFoundException If the resource file cannot be found or read.
     */
    @Override
    public byte[] loadTempResource(final String filename, final String sessionId) {
        Path tempPath = tempLocation.resolve(sessionId).normalize();
        return getResourceFromPath(filename, tempPath);
    }

    /**
     * Store a multipart file as a temporary resource.
     *
     * @param file      The {@link MultipartFile} to store.
     * @param sessionId The ID of the session associated with the temporary resource.
     * @return The name of the stored file.
     * @throws StorageException If an error occurs while storing the file.
     */
    @Override
    public String storeToTemp(final MultipartFile file, final String sessionId) {
        String name = UUID.randomUUID() + file.getName();
        Path sessionTempPath = this.tempLocation
                .resolve(Paths.get(sessionId))
                .normalize();

        try {
            var updatedFile = new CustomMultipartFile(
                    name,
                    name,
                    file.getContentType(),
                    file.getInputStream());
            FileSystemStorageUtils.storeToPath(updatedFile, sessionTempPath);

            return name;
        } catch (IOException e) {
            throw new StorageException("Failed to read MultipartFile data.");
        }
    }


    /**
     * Persist and process a list of filenames associated with a session.
     *
     * @param filenames The list of filenames to persist and process.
     * @param sessionId The ID of the session associated with the files.
     * @throws StorageException If an error occurs while persisting or processing the files.
     */
    @Override
    public void persist(final List<String> filenames, final String sessionId) {
        Path tempPath = tempLocation.resolve(sessionId).normalize();
        var tempFilePaths = filenames.stream()
                .map((String filename) -> FileSystemStorageUtils.loadPath(filename, tempPath))
                .collect(Collectors.toList());
        try {
            for (Path fp : tempFilePaths) {
                var imageFile = new File(String.valueOf(fp));
                CustomMultipartFile inputFile = new CustomMultipartFile(
                        imageFile.getName(),
                        imageFile.getName(),
                        "image/jpeg",
                        new FileInputStream(imageFile));
                FileSystemStorageUtils.storeToPath(inputFile, originalLocation);

                var compressedFile = ImageCompressor.compress(
                        inputFile,
                        TARGET_WIDTH,
                        QUALITY);
                FileSystemStorageUtils.storeToPath(compressedFile, previewLocation);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files");
        }
        try {
            FileSystemUtils.deleteRecursively(tempPath);
        } catch (IOException e) {
            throw new StorageException("Could not delete session temp folder");
        }
    }

    /**
     * Retrieve a resource from the specified path and return it as a byte array.
     *
     * @param filename    The name of the resource file.
     * @param currentPath The path where the resource is located.
     * @return A byte array containing the resource data.
     * @throws StorageFileNotFoundException If the resource file cannot be found or read.
     */
    private static byte[] getResourceFromPath(final String filename, final Path currentPath) {
        try {
            var file = FileSystemStorageUtils.loadPath(filename, currentPath);
            var resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource.getContentAsByteArray();
            } else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);
            }
        } catch (IOException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename);
        }
    }

}
