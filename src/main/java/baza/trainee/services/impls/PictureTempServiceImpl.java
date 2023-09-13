package baza.trainee.services.impls;

import baza.trainee.exceptions.StorageException;
import baza.trainee.services.PictureService;
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
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Service that implements {@link PictureTempService} contract.
 *
 * @author Andry Sitarskiy
 */

@Service
public class PictureTempServiceImpl implements PictureTempService {
    @Value("${uploads.path}")
    private String uploadPath;
    @Value("${prefix.response}")
    private String prefixResponse;

    @Value("${dir.temp}")
    private String temp;
    @Value("${dir.original}")
    private String original;
    @Value("$dir.preview")
    String preview;

    private static final int TARGET_WIDTH = 680;
    private static final float QUALITY = 0.5F;

    private Path rootLocation;
    private Path dirResponse;

    @PostConstruct
    public void init() {
        this.rootLocation = Path.of(System.getProperty("user.dir"), uploadPath);
        dirResponse = Path.of(prefixResponse);
    }


    @Override
    public String addPicture(MultipartFile newPicture, String dest) {
        return createFile(newPicture, Path.of(temp, dest)).replace(temp, "");
    }

    @Override
    public List<String> moveAndCompressionFileToFolder(
            List<String> oldPathsFile, String dest) {
        StringBuilder errors = new StringBuilder();
        final List<String> pathMoveFiles = oldPathsFile.stream().map(path -> {
            Path oldPath = rootLocation.resolve(temp).resolve(path).normalize();
            Path destOriginalPath =
                    rootLocation.resolve(original).resolve(dest).resolve(path)
                            .normalize();
            Path destPreviewPath =
                    rootLocation.resolve(preview).resolve(dest).resolve(path)
                            .normalize();
            if (Files.exists(oldPath)) {
                try {
                    Files.copy(oldPath, destOriginalPath);
                } catch (IOException e) {
                    errors.append("Not move file ").append(path).append(" \\n");
                }
                try {
                    ImageCompressor.compress(oldPath.toFile(), TARGET_WIDTH,
                                    QUALITY)
                            .transferTo(destPreviewPath);
                } catch (IOException e) {
                    errors.append("Not compression file ").append(path)
                            .append(" \\n");
                }
            } else {
                errors.append("Not file ").append(path).append(" \\n");
            }
            return Path.of(dest, path).toString();
        }).toList();
        try {
            FileSystemUtils.deleteRecursively(rootLocation.resolve(temp));
        } catch (IOException e) {
            throw new StorageException("Not delete directory " + dest);
        }
        if (!errors.isEmpty()) {
            throw new StorageException(errors.toString());
        }
        return pathMoveFiles;
    }

    @Override
    public void deleteFilesInFolders(List<String> pathsFile) {
        StringBuilder errors = new StringBuilder();
        pathsFile.forEach(path -> {
            Path originalPath = rootLocation.resolve(original).resolve(path);
            try {
                Files.delete(originalPath);
            } catch (IOException e) {
                errors.append("Not delete original file ").append(path)
                        .append("\\n");
            }
            Path previewPath = rootLocation.resolve(preview).resolve(path);
            try {
                Files.delete(previewPath);
            } catch (IOException e) {
                errors.append("Not delete preview file ").append(path)
                        .append("\\n");
            }
        });
    }

    @Override
    public List<String> updateFilesInFolder(List<String> pathsFile,
                                            String dest) {
        final Path originalDir = rootLocation.resolve(original).resolve(dest);
        final Path previewDir = rootLocation.resolve(preview).resolve(dest);
        final StringBuilder errors =
                deleteFileWithoutPath(pathsFile, originalDir)
                        .append(deleteFileWithoutPath(pathsFile, previewDir));
        if (!errors.isEmpty()) {
            throw new StorageException(errors.toString());
        }

        return moveAndCompressionNewFiles(pathsFile, originalDir, dest);
    }

    private List<String> moveAndCompressionNewFiles(List<String> pathsFile,
                                                    Path originalDir,
                                                    String dest) {
        try (final Stream<Path> pathDir = Files.walk(originalDir,
                Integer.MAX_VALUE)) {
            List<String> leaveFiles = pathDir
                    .filter(path -> pathsFile.contains(
                            originalDir.relativize(path).toString()))
                    .map(path -> originalDir.relativize(path).toString())
                    .toList();
            List<String> moveAndCompressionNewFiles = pathsFile.stream()
                    .filter(leaveFiles::contains).toList();
            List<String> newFiles =
                    moveAndCompressionFileToFolder(moveAndCompressionNewFiles,
                            dest);
            return Stream.of(leaveFiles, newFiles).flatMap(Collection::stream).toList();

        } catch (IOException e) {
            throw new StorageException(
                    "Not delete files in directory " + originalDir);
        }
    }

    private StringBuilder deleteFileWithoutPath(List<String> pathsFile,
                                                Path dir) {
        StringBuilder errors = new StringBuilder();
        try (final Stream<Path> pathDir = Files.walk(dir, Integer.MAX_VALUE)) {
            pathDir.filter(path -> !pathsFile.contains(
                            dir.relativize(path).toString()))
                    .forEach(pathDelete -> {
                        try {
                            Files.delete(pathDelete);
                        } catch (IOException e) {
                            errors.append("Not delete file ").append(pathDelete)
                                    .append("\\n");
                        }
                    });
        } catch (IOException e) {
            errors.append("Not delete files in directory ").append(dir)
                    .append("\\n");
        }
        return errors;
    }

    private String createFile(MultipartFile newPicture, Path dir) {
        if (newPicture != null && newPicture.getName().isBlank()) {
            Path newNameFile = createNewNameFile(newPicture.getName());
            final Path newPathDir = rootLocation.resolve(dir);

            try {
                if (!Files.exists(newPathDir)) {
                    Files.createDirectory(newPathDir);
                }

                newPicture.transferTo(newPathDir.resolve(newNameFile));
            } catch (IOException e) {
                throw new StorageException(
                        "Not create file" + newPicture.getOriginalFilename());
            }

            return dirResponse.resolve(dir)
                    .resolve(newNameFile).toString();
        } else {
            throw new StorageException(
                    "Not file or not file name for create file");
        }
    }

    private Path createNewNameFile(String originalFileName) {
        return Path.of(UUID.randomUUID() + originalFileName);
    }
}
