package baza.trainee.services.impls;

import baza.trainee.exceptions.StorageException;
import baza.trainee.exceptions.StorageFileNotFoundException;
import baza.trainee.services.ResourcePictureFromCacheOrBaseService;
import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import io.quarkus.cache.CaffeineCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class ResourcePictureFromCacheOrBaseServiceImpl implements
        ResourcePictureFromCacheOrBaseService {
    @Value("${uploads.path}")
    private String uploadPath;

    @CacheName("resourcePicture")
    private Cache cache;

    @Override
    public Resource loadAsResource(String shortPathPicture) {
        final Resource resource = getFromCache(shortPathPicture);
        return Objects.requireNonNullElseGet(resource,
                () -> getFromBase(shortPathPicture));
    }

    private Resource getFromBase(String shortPathPicture) {
        try {
            var file = load(shortPathPicture);
            var resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + shortPathPicture);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " +
                    shortPathPicture, e);
        }
    }

    private Resource getFromCache(String shortPathPicture) {
        MultipartFile picture = null;
        try {
            picture = (MultipartFile) cache.as(CaffeineCache.class)
                    .getIfPresent(shortPathPicture).get();
        } catch (InterruptedException e) {
            log.warn("Cache is not file " + shortPathPicture);
            Thread.currentThread().interrupt();
        } catch (ExecutionException | ClassCastException e) {
            log.warn("Cache is not file " + shortPathPicture);
        }
        return picture != null ? picture.getResource() : null;
    }

    private Path load(String filename) {
        return Path.of(System.getProperty("user.dir"), uploadPath, filename)
                .normalize().toAbsolutePath();
    }
}
