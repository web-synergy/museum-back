package baza.trainee.services.impls;

import baza.trainee.services.PictureService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

@Service
public class PictureServiceImpl implements PictureService {

    @Value("${uploads.path}")
    private String uploadPath;

    @Value("${prefix.response}")
    private String prefixResponse;

    private Path rootLocation;
    private Path dirResponse;

    @PostConstruct
    public void init() {
        this.rootLocation = Path.of(System.getProperty("user.dir"), uploadPath);
        this.dirResponse = Path.of(prefixResponse);
    }

    @Override
    public String addPicture(MultipartFile newPicture, String ownDir) throws IOException {
        return createFile(newPicture, Path.of(ownDir));
    }

    private Path createNewNameFile(String originalFileName) {
        return Path.of(LocalDate.now().toString(), originalFileName);
    }

    private String createFile(MultipartFile newFile, Path dir)
            throws IOException {
        if (newFile != null && newFile.getOriginalFilename() != null) {


            if (!Files.exists(dir)) {
                Files.createDirectory(dir);
            }

            newFile.transferTo(dir.resolve(createNewNameFile(newFile.getOriginalFilename())));

            Path pathAfterUploads = rootLocation.relativize(dir);
            return dirResponse.resolve(pathAfterUploads)
                    .resolve(newFile.getOriginalFilename()).toString();
        } else {
            throw new IOException("Not file or not file name");
        }

    }

    @Override
    public String changePicture(String oldPath, MultipartFile newPicture)
            throws IOException {
        if (!deletePicture(oldPath)) {
            throw new IOException("Not delete file");
        }

        Path oldFullPath =
                rootLocation.resolve(dirResponse.relativize(Path.of(oldPath)));

        return createFile(newPicture, oldFullPath.getParent());
    }

    @Override
    public boolean deletePicture(String oldPath) throws IOException {
        if (oldPath != null && oldPath.isBlank()) {
            Path oldPathFile = rootLocation.resolve(
                    dirResponse.relativize(Path.of(oldPath)));

            if (Files.exists(oldPathFile)) {
                Files.delete(oldPathFile);
            }
            return !Files.exists(oldPathFile);
        }
        return false;
    }

}
