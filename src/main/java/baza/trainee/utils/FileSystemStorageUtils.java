package baza.trainee.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.*;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import baza.trainee.exceptions.custom.StorageException;
import baza.trainee.exceptions.custom.StorageFileNotFoundException;

/**
 * Utility class for managing file system storage operations.
 * This class provides methods for loading file paths, storing files, and
 * initializing directories.
 *
 * @author Evhen Malysh
 */
public class FileSystemStorageUtils {

    private static final String FAILED_TO_FIND_FILES = "Failed to find files";

    /**
     * Private constructor to prevent instantiation of this utility class.
     *
     * @throws IllegalStateException with the message "Utility class."
     */
    private FileSystemStorageUtils() {
        throw new IllegalStateException("Utility class.");
    }

    /**
     * Load the file path by filename in the provided directory.
     *
     * @param root     The root directory where the file is located.
     * @return The {@link Path} of the file.
     * @throws StorageException If an error occurs while reading the stored files.
     */
    public static Path loadPath(final Path root) {
        try (var pathStream = Files.walk(root, FileVisitOption.FOLLOW_LINKS)) {
            return pathStream
                    .filter(path -> path.toFile().isDirectory())
                    .findFirst()
                    .orElseThrow(() -> new StorageException(FAILED_TO_FIND_FILES));
        } catch (IOException e) {
            throw new StorageException(FAILED_TO_FIND_FILES);
        }
    }

    public static Path loadFilePath(final Path root) {
        try (var pathStream = Files.walk(root, FileVisitOption.FOLLOW_LINKS)) {
            return pathStream
                    .filter(path -> !path.toFile().isDirectory())
                    .findFirst()
                    .orElseThrow(() -> new StorageException(FAILED_TO_FIND_FILES));
        } catch (IOException e) {
            throw new StorageException(FAILED_TO_FIND_FILES);
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
     * @param paths An iterable of {@link Path} objects representing the directories
     *              to create.
     * @throws StorageException If an error occurs while initializing storage
     *                          directories.
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

    /**
     * Wrap {@link FileSystemUtils} copyRecursively method, and rethrow
     * specified {@link StorageException}.
     *
     * @param src  Source {@link Path}.
     * @param dest Destination {@link Path}.
     * @throws StorageException in the case of I/O Errors.
     */
    public static void copyRecursively(Path src, Path dest) {
        try {
            FileSystemUtils.copyRecursively(src, dest);
        } catch (IOException e) {
            throw new StorageException("Failed to copy files from: " + src + " to: " + dest);
        }
    }

    /**
     * Wrap {@link FileSystemUtils} deleteRecursively method, and rethrow
     * specified {@link StorageException}.
     *
     * @param root Root {@link Path} to delete.
     * @throws StorageException in the case of I/O Errors.
     */
    public static void deleteRecursively(Path root) {
        try {
            FileSystemUtils.deleteRecursively(root);
        } catch (IOException e) {
            throw new StorageException("Could not delete session temp folder: " + root);
        }
    }

    /**
     * Create {@link UrlResource} based on given {@link URI} object.
     *
     * @param uri {@link URI} to resource.
     * @return {@link UrlResource}.
     * @throws StorageFileNotFoundException if the given URI path is not
     *                valid.
     */
    public static UrlResource getResource(URI uri) {
        try {
            return new UrlResource(uri);
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file from URI: " + uri);
        }
    }

    /**
     * Returns content of {@link Resource} as byte array.
     *
     * @param resource {@link Resource} to read.
     * @return content as array of bytes.
     * @throws StorageFileNotFoundException if could not read recourse.
     */
    public static byte[] getByteArrayFromResource(Resource resource) {
        try {
            if (resource.exists() || resource.isReadable()) {
                return resource.getContentAsByteArray();
            } else {
                throw new StorageFileNotFoundException("Could not read resource.");
            }
        } catch (IOException e) {
            throw new StorageFileNotFoundException("Could not read resource.");
        }
    }

}
