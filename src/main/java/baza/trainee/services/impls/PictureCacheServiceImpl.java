package baza.trainee.services.impls;

import baza.trainee.exceptions.StorageException;
import baza.trainee.services.PictureCacheService;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.time.LocalDate;

public class PictureCacheServiceImpl implements PictureCacheService {
    @Value("${prefix.cache}")
    private String prefixResponse;


    private Path dirResponse;

    @PostConstruct
    public void init() {
        this.dirResponse = Path.of(prefixResponse);
    }

    @Override
    public String addPicture(MultipartFile newPicture, String ownDir) {
        return createFile(newPicture, Path.of(ownDir));
    }

    private String createFile(MultipartFile newPicture, Path dir) {
        if (newPicture != null && newPicture.getOriginalFilename() != null) {
            saveInCachePicture(newPicture, dir
                    .resolve(newPicture.getOriginalFilename()).toString());

            Path newPathFile = dirResponse.resolve(dir)
                    .resolve(createNewNameFile(newPicture.getOriginalFilename()));
            return newPathFile.toString();
        }else {throw new StorageException("Not file or not file name for create file");}
    }

    @CacheResult(cacheName = "pictureResource")
    private MultipartFile saveInCachePicture(MultipartFile newPicture, @CacheKey String originalFilename) {
        return newPicture;
    }

    private Path createNewNameFile(String originalFileName) {
        return Path.of(LocalDate.now().toString(), originalFileName);
    }

    @Override
    public String changePicture(String oldPathFile, MultipartFile newPicture) {
        deletePicture(oldPathFile);

        Path oldPathWithoutPrefix = dirResponse.relativize(Path.of(oldPathFile));

        return createFile(newPicture, oldPathWithoutPrefix.getParent());
    }

    @Override
    @CacheInvalidate(cacheName = "pictureResource")
    public void deletePicture(@CacheKey String oldPathFile) {
        if (oldPathFile == null){throw new StorageException("Not path of file");}
    }
}
