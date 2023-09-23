package baza.trainee.services.impls;

import baza.trainee.enums.TypePicture;
import baza.trainee.exceptions.StorageException;
import baza.trainee.services.PictureTempService;
import baza.trainee.util.ImageCompressor;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Service that implements {@link PictureTempService} contract.
 *
 * @author Andry Sitarskiy
 */

@Service
public class PictureTempServiceImpl implements PictureTempService {

    /**
     * Name root directory for write files.
     */
    @Value("${uploads.path}")
    private String uploadPath;
    /**
     * Name temp directory for write files.
     */
    @Value("${dir.temp}")
    private String temp;
    /**
     * Name destination directory for write files.
     */
    private String destDir;
    /**
     * Full path of root directory.
     */
    private Path rootLocation;

    /**
     * Path initialization of root directory.
     */
    @PostConstruct
    public void init() {
        rootLocation = Path.of(System.getProperty("user.dir"), uploadPath);
    }

    /**
     * Add and compression picture in rootLocation/temp/userId/{type} directory.
     *
     * @param newPicture file from form
     * @param userId     Id user
     * @param dest       uuid directory
     * @return Short path of rootLocation/temp/userId/{type} directory
     */
    @Override
    public String addPicture(final MultipartFile newPicture,
                             final String userId,
                             final String dest) {
        String result;
        if (newPicture != null && !newPicture.getName().isBlank()) {
            for (TypePicture value : TypePicture.values()) {
                Path newPathDir = Path.of(rootLocation.toString(), temp, userId,
                                value.name().toLowerCase(), dest)
                        .normalize().toAbsolutePath();
                createDir(newPathDir);

                createFileOrCompressionFile(newPicture, value, newPathDir);
            }
            result = Path.of(dest, newPicture.getOriginalFilename()).toString();
        } else {
            throw new StorageException(
                    "Not file or not file name for create file");
        }
        return result;
    }


    private void createFileOrCompressionFile(final MultipartFile newPicture,
                                             final TypePicture value,
                                             final Path newPathDir)
            throws StorageException {
        if (value == TypePicture.ORIGINAL) {
            try {
                newPicture.transferTo(
                        newPathDir.resolve(Objects.requireNonNull(
                                newPicture.getOriginalFilename())));
            } catch (IOException e) {
                throw new StorageException(
                        "Not create original file "
                                + newPicture.getOriginalFilename());
            }
        } else {
            try {
                final MultipartFile compressionFile =
                        ImageCompressor.compress(newPicture,
                                value.getTargetWidth(), value.getQuality());
                compressionFile.transferTo(newPathDir
                        .resolve(Objects.requireNonNull(
                                compressionFile.getOriginalFilename())));
            } catch (IOException e) {
                throw new StorageException(
                        "Not compression " + value.name().toLowerCase()
                                + " file " + newPicture.getName());
            }
        }
    }


    private void createDir(final Path newPathDir) {
        if (!Files.exists(newPathDir)) {
            try {
                Files.createDirectories(newPathDir);
            } catch (IOException e) {
                throw new StorageException(
                        "Not create directory " + newPathDir);
            }
        }
    }

    /**
     * Move  files in temp directory to rootLocation/{type} directory.
     *
     * @param sourcePathsFile List of short paths in temp directory
     * @param userId          Id user
     **/
    @Override
    public void moveFilesInTempToFolder(final List<String> sourcePathsFile,
                                        final String userId) {
        Path pathSourceDir = rootLocation.resolve(temp).resolve(
                userId);
        moveFilesToFolder(sourcePathsFile, pathSourceDir, rootLocation);
        destDir = null;
    }

    /**
     * Move files in rootLocation/{type} directory to temp directory.
     *
     * @param sourcePathsFile List of short paths in temp directory
     * @param userId          Userid
     **/
    @Override
    public void moveFilesInFolderToTemp(final List<String> sourcePathsFile,
                                        final String userId) {
        Path pathDestDir = rootLocation.resolve(temp).resolve(
                userId);
        moveFilesToFolder(sourcePathsFile, rootLocation, pathDestDir);
        destDir = Path.of(sourcePathsFile.get(0)).getName(0).toString();
    }

    /**
     * Move files in source directory to destination directory and
     * delete source directory.
     *
     * @param sourcePathsFile paths files of source move in
     *                        destination directory.
     * @param sourceDir       path source directory.
     * @param destinationDir  path destination directory.
     */
    private void moveFilesToFolder(final List<String> sourcePathsFile,
                                   final Path sourceDir,
                                   final Path destinationDir) {
        StringBuilder errors = new StringBuilder();
        for (TypePicture value : TypePicture.values()) {
            sourcePathsFile.forEach(path -> {
                Path absoluteOldPath = sourceDir
                        .resolve(value.name().toLowerCase())
                        .resolve(path)
                        .normalize().toAbsolutePath();
                Path absoluteNewPath =
                        destinationDir.resolve(value.name().toLowerCase())
                                .resolve(path)
                                .normalize().toAbsolutePath();
                try {
                    Files.move(absoluteOldPath, absoluteNewPath);
                } catch (IOException e) {
                    errors.append("Not move file ").append(absoluteOldPath)
                            .append(" \\n");
                }
            });

            Path pathRootDir = Path.of(value.name().toLowerCase()).resolve(
                    Path.of(sourcePathsFile.get(0)).getName(0));
            Path pathDeleteDir = sourceDir.resolve(pathRootDir)
                    .normalize().toAbsolutePath();
            deletePartDir(errors, pathDeleteDir);
        }


        if (!errors.isEmpty()) {
            throw new StorageException(errors.toString());
        }
    }

    /**
     * Delete directories in folder rootLocation/{type} and temp.
     *
     * @param pathDeleteDir short path directory in directory
     *                      rootLocation/{type}.
     * @param userId        User id
     */
    @Override
    public void deleteDirectory(final String pathDeleteDir,
                                final String userId) {
        StringBuilder errors = new StringBuilder();
        for (TypePicture value : TypePicture.values()) {
            Path pathDirInRoot =
                    rootLocation.resolve(value.name().toLowerCase()).
                            resolve(pathDeleteDir).normalize().toAbsolutePath();
            if (Files.exists(pathDirInRoot)) {
                deletePartDir(errors, pathDirInRoot);
            }

            Path pathDirInTemp = rootLocation.resolve(userId)
                    .resolve(value.name().toLowerCase()).resolve(pathDeleteDir)
                    .normalize().toAbsolutePath();
            if (Files.exists(pathDirInTemp)) {
                deletePartDir(errors, pathDirInTemp);
            }
        }
        if (!errors.isEmpty()) {
            throw new StorageException(errors.toString());
        }
        destDir = null;
    }

    /**
     * Delete directory.
     *
     * @param errors  log error processing*
     * @param pathDir Absolut path directory
     */
    private void deletePartDir(final StringBuilder errors, final Path pathDir) {
        try {
            FileSystemUtils.deleteRecursively(pathDir);
        } catch (IOException e) {
            errors.append("Not delete directory ").append(pathDir)
                    .append("\\n");
        }
    }


    /**
     * Get uuid name directory.
     *
     * @return Uuid name directory
     */
    @Override
    public String getDir() {
        if (destDir == null) {
            destDir = UUID.randomUUID().toString();
        }
        return destDir;
    }

}
