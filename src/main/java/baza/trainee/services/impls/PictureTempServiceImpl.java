package baza.trainee.services.impls;

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
    String preview ;

    private static final int TARGET_WIDTH = 680;
    private static final float QUALITY = 0.5F;

    private Path rootLocation;


    private final PictureServiceImpl pictureService;

    @PostConstruct
    public void init() {
        this.rootLocation = Path.of(System.getProperty("user.dir"), uploadPath);
    }

    public PictureTempServiceImpl(PictureServiceImpl pictureService) {
        this.pictureService = pictureService;
    }

    @Override
    public String addPicture(MultipartFile newPicture, String ownDir) {
        return pictureService.addPicture(newPicture, Path.of(temp,ownDir).toString())
                .replace(temp,"");
    }

    @Override
    public void moveToFolder(List<String> oldPathsFile, String dest) {
        StringBuilder errors = new StringBuilder();
        oldPathsFile.forEach(path ->{
            Path oldPath = rootLocation.resolve(temp).resolve(path).normalize();
            Path destOriginalPath = rootLocation.resolve(original).resolve(dest).resolve(path).normalize();
            Path destPreviewPath = rootLocation.resolve(preview).resolve(dest).resolve(path).normalize();
            if (Files.exists(oldPath)){
                try {
                    Files.copy(oldPath, destOriginalPath);
                } catch (IOException e) {
                    errors.append("Not copy file ").append(path).append(" \\n");
                }
                try {
                    ImageCompressor.compress(oldPath.toFile(),TARGET_WIDTH,QUALITY)
                            .transferTo(destPreviewPath);
                } catch (IOException e) {
                    errors.append("Not compression file ").append(path).append(" \\n");
                }
            } else {errors.append("Not file ").append(path).append(" \\n");}
        });
        try {
            FileSystemUtils.deleteRecursively(rootLocation.resolve(temp));
        } catch (IOException e) {
            throw new StorageException("Not delete directory " + dest);
        }
        if (!errors.isEmpty()){throw new StorageException(errors.toString());}
    }
}
