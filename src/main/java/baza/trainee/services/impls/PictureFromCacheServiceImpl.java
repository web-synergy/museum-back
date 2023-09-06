package baza.trainee.services.impls;

import baza.trainee.exceptions.StorageException;
import baza.trainee.services.PictureFromCacheService;
import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import io.quarkus.cache.CaffeineCache;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class PictureFromCacheServiceImpl implements PictureFromCacheService {
    @CacheName("resourcePicture")
    private Cache cache;

    @Value("${uploads.path}")
    private String uploadPath;

    private Path rootLocation;

    @PostConstruct
    public void init() {
        this.rootLocation = Path.of(System.getProperty("user.dir"), uploadPath);

    }

    @Override
    public boolean addPicture(String shortPathPicture) {
        MultipartFile picture;
        try {
            picture = (MultipartFile) cache.as(CaffeineCache.class).getIfPresent(shortPathPicture).get();
        } catch (InterruptedException | ClassCastException e) {
            Thread.currentThread().interrupt();
            throw new StorageException("Cache is not file " + shortPathPicture);
        } catch (ExecutionException e) {
            throw new StorageException("Cache is not file " + shortPathPicture);
        }
        if (picture != null){
            try {
                picture.transferTo(rootLocation.resolve(shortPathPicture));
            } catch (IOException e) {
                throw new StorageException("Not create file " + shortPathPicture);
            }
        }
        return picture != null;
    }

    @Override
    public boolean addPictures(List<String> shortPathsPicture) {
        final StringBuilder errors = new StringBuilder();
        shortPathsPicture.forEach(path -> {
            try {
                addPicture(path);
            } catch (StorageException e) {
                errors.append(e.getMessage()).append("<br>");
            }
        });
        if (!errors.isEmpty()){throw new StorageException(errors.toString());}
        return true;
    }

    @Override
    public boolean deletePicture(String shortPathPicture) {
        Path oldPathFile = rootLocation.resolve(shortPathPicture);
        if (Files.exists(oldPathFile)) {
            try {
                Files.delete(oldPathFile);
            } catch (IOException e) {
                throw new StorageException("Not delete file " + shortPathPicture);
            }
        } else {throw new StorageException("Not file " + shortPathPicture);}
        return true;
    }

    @Override
    public boolean deletePictures(List<String> shortPathsPicture) {
        final StringBuilder errors = new StringBuilder();
        shortPathsPicture.forEach(path -> {
            try {
                deletePicture(path);
            } catch (StorageException e) {
                errors.append(e.getMessage()).append("<br>");
            }
        });
        if (!errors.isEmpty()){throw new StorageException(errors.toString());}
        return true;
    }
}
