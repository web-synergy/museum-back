package baza.trainee.services.impls;

import baza.trainee.exceptions.StorageException;
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
    public String addPicture(MultipartFile newPicture, String ownDir) {
        return createFile(newPicture, Path.of(ownDir));
    }

    private Path createNewNameFile(String originalFileName) {
        return Path.of(LocalDate.now().toString(), originalFileName);
    }

    private String createFile(MultipartFile newFile, Path dir){
        if (newFile != null && newFile.getOriginalFilename() != null) {
            Path newNameFile = createNewNameFile(newFile.getOriginalFilename());

            try {
                if (!Files.exists(dir)) {
                    Files.createDirectory(dir);
                }

                newFile.transferTo(dir.resolve(newNameFile));
            } catch (IOException e) {
                throw new StorageException("Not create file" + newFile.getOriginalFilename());
            }

            Path pathAfterUploads = rootLocation.relativize(dir);
            return dirResponse.resolve(pathAfterUploads)
                    .resolve(newNameFile).toString();
        } else {
            throw new StorageException("Not file or not file name for create file");
        }

    }

    @Override
    public String changePicture(String oldPath, MultipartFile newPicture)
             {
        if (!deletePicture(oldPath)) {
            throw new StorageException("Not file or not file name for delete file ");
        }

        Path oldFullPath =
                rootLocation.resolve(dirResponse.relativize(Path.of(oldPath)));

        return createFile(newPicture, oldFullPath.getParent());
    }

    @Override
    public boolean deletePicture(String oldPath) {
        if (oldPath != null && oldPath.isBlank()) {
            Path oldPathFile = rootLocation.resolve(
                    dirResponse.relativize(Path.of(oldPath)));

            if (Files.exists(oldPathFile)) {
                try {
                    Files.delete(oldPathFile);
                } catch (IOException e) {
                    throw new StorageException("Not delete file " + oldPath);
                }
            }
            return !Files.exists(oldPathFile);
        }
        return false;
    }

}
