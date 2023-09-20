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

    @Value("${dir.temp}")
    private String temp;

    private Path rootLocation;


    @PostConstruct
    public void init() {
        rootLocation = Path.of(System.getProperty("user.dir"), uploadPath);
    }


    @Override
    public String addPicture(MultipartFile newPicture, String userId,
                             String dest) {
        if (newPicture != null && newPicture.getName().isBlank()) {
            for (TypePicture value : TypePicture.values()) {
                Path newPathDir = Path.of(rootLocation.toString(), temp, userId,
                                value.name().toLowerCase(), dest)
                        .normalize().toAbsolutePath();
                createDir(newPathDir);

                createFileOrCompressionFile(newPicture, value, newPathDir);
            }
            return Path.of(dest,newPicture.getName()).toString();
        } else {
            throw new StorageException(
                    "Not file or not file name for create file");
        }

    }


    private void createFileOrCompressionFile(MultipartFile newPicture, TypePicture value,
                           Path newPathDir) {
        if (value == TypePicture.ORIGINAL) {
            try {
                newPicture.transferTo(newPathDir.resolve(newPicture.getName()));
            } catch (IOException e) {
                throw new StorageException("Not create original file " +
                        newPicture.getName());
            }
        } else {
            try {
                ImageCompressor.compress(newPicture, value.getTargetWidth(),
                                value.getQuality()).transferTo(newPathDir);
            } catch (IOException e) {
                throw new StorageException("Not create " + value.name().toLowerCase() +
                        " file " + newPicture.getName());
            }
        }
    }

    private void createDir(Path newPathDir) {
        if (!Files.exists(newPathDir)) {
            try {
                Files.createDirectory(newPathDir);
            } catch (IOException e) {
                throw new StorageException("Not create directory " + newPathDir);
            }
        }
    }

    @Override
    public void moveFilesInTempToFolder(List<String> sourcePathsFile, String userId) {
        Path pathSourceDir = rootLocation.resolve(temp).resolve(
                userId);
        moveFilesToFolder(sourcePathsFile, pathSourceDir, rootLocation);
    }

    @Override
    public void moveFilesInFolderToTemp(List<String> sourcePathsFile, String userId) {
        Path pathDestDir = rootLocation.resolve(temp).resolve(
                userId);
        moveFilesToFolder(sourcePathsFile, rootLocation, pathDestDir);
    }

    /**
     * Move files in source directory to destination directory and delete source directory
     *
     * @param sourcePathsFile  paths files of source move in destination directory
     * @param sourceDir path source directory
     * @param destDir path destination directory*/
    private void moveFilesToFolder(List<String> sourcePathsFile, Path sourceDir,
                                   Path destDir) {
        StringBuilder errors = new StringBuilder();
        for (TypePicture value : TypePicture.values()){
            sourcePathsFile.forEach(path -> {
                Path absoluteOldPath = sourceDir
                        .resolve(value.name().toLowerCase())
                        .resolve(path)
                        .normalize().toAbsolutePath();
                Path absoluteNewPath = destDir.resolve(value.name().toLowerCase())
                        .resolve(path)
                        .normalize().toAbsolutePath();
                try {
                    Files.move(absoluteOldPath, absoluteNewPath);
                } catch (IOException e) {
                    errors.append("Not move file ").append(absoluteOldPath).append(" \\n");
                }
            });

            Path rootDirInTemp = Path.of(value.name().toLowerCase()).resolve(
                    Path.of(sourcePathsFile.get(0)).getName(0));
            Path pathDeleteDir = sourceDir.resolve(rootDirInTemp)
                    .normalize().toAbsolutePath();
            deletePartDir(errors, pathDeleteDir);
        }


        if (!errors.isEmpty()) {
            throw new StorageException(errors.toString());
        }
    }

    @Override
    public void deleteDirectory(String pathsDeleteDir, String userId) {
        StringBuilder errors = new StringBuilder();
        for (TypePicture value : TypePicture.values()){
            Path pathDirInRoot =rootLocation.resolve(value.name().toLowerCase()).
                    resolve(pathsDeleteDir).normalize().toAbsolutePath();
            deletePartDir(errors, pathDirInRoot);

            Path pathDirInTemp =rootLocation.resolve(userId).resolve(value.name().toLowerCase()).
                    resolve(pathsDeleteDir).normalize().toAbsolutePath();
            deletePartDir(errors, pathDirInTemp);
        }
        if (!errors.isEmpty()) {
            throw new StorageException(errors.toString());
        }
    }

    /**
     * Delete directory
     *
     * @param errors log error processing*
     * @param pathDir Absolut path directory*/
    private void deletePartDir(StringBuilder errors, Path pathDir) {
        try {
            FileSystemUtils.deleteRecursively(pathDir);
        } catch (IOException e) {
            errors.append("Not delete directory ").append(pathDir)
                    .append("\\n");
        }
    }


    @Override
    public String fullPath(String[] paths) {
        return Path.of("", paths).toString();
    }

    @Override
    public String createDir() {
        return UUID.randomUUID().toString();
    }

}
