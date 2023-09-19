package baza.trainee.utils;

import baza.trainee.exceptions.custom.StorageException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Utility class for managing file system storage operations.
 * This class provides methods for loading file paths, storing files, and initializing directories.
 *
 * @author Evhen Malysh
 */
public class FileSystemStorageUtils {

    /**
     * Private constructor to prevent instantiation of this utility class.
     * Throws an {@link IllegalStateException} with the message "Utility class."
     */
    private FileSystemStorageUtils() {
        throw new IllegalStateException("Utility class.");
    }

    /**
     * Load the file path by filename in the provided directory.
     *
     * @param filename The name of the file.
     * @param location The root directory where the file is located.
     * @return The {@link Path} of the file.
     * @throws StorageException If an error occurs while reading the stored files.
     */
    public static Path loadPath(final String filename, final Path location) {
        try (var pathStream = Files.walk(location, Integer.MAX_VALUE)) {
            return pathStream
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().equals(Paths.get(filename)))
                    .findFirst()
                    .orElseThrow(() -> new StorageException("Failed to find files"));
        } catch (IOException e) {
            throw new StorageException("Failed to find files");
        }
    }

    /**
     * Store a {@link MultipartFile} to the given directory.
     *
     * @param file     The {@link MultipartFile} to store.
     * @param location The {@link Path} where the file should be stored.
     * @throws StorageException If an error occurs while storing the file.
     */
    public static void storeToPath(final MultipartFile file, final Path location) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }
            Files.createDirectories(location);

            Path destinationFile = location
                    .resolve(Paths.get(file.getName()))
                    .normalize()
                    .toAbsolutePath();
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream,
                        destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file.");
        }
    }

    /**
     * Create directories for the provided paths.
     *
     * @param paths An iterable of {@link Path} objects representing the directories to create.
     * @throws StorageException If an error occurs while initializing storage directories.
     */
    public static void init(final Path... paths) {
        try {
            for (Path path : paths) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage");
        }
    }
}
