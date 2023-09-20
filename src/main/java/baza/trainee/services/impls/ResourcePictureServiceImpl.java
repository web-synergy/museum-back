package baza.trainee.services.impls;

import baza.trainee.exceptions.StorageFileNotFoundException;
import baza.trainee.services.ResourcePictureService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

@Service
public class ResourcePictureServiceImpl implements ResourcePictureService {
    @Value("${uploads.path}")
    private String uploadPath;

    @Override
    public byte[] loadAsResource(String type, String filename) {
        try {
            var file = load(type,filename);
            var resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource.getContentAsByteArray();
            } else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);

            }
        } catch (IOException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    private Path load(String type, String filename) {
        return Path.of(System.getProperty("user.dir"), uploadPath, type, filename)
                .normalize().toAbsolutePath();
    }
}
