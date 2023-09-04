package baza.trainee.services.impls;

import baza.trainee.services.ResourcePictureService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

@Service
public class ResourcePictureServiceImpl implements ResourcePictureService {
    @Value("${uploads.path}")
    private String uploadPath;

    @Override
    public Resource loadAsResource(String filename) throws IOException {
        try {
            var file = load(filename);
            var resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new IOException(
                        "Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new IOException("Could not read file: " + filename, e);
        }
    }

    private Path load(String filename) {
        return Path.of(System.getProperty("user.dir"), uploadPath, filename)
                .normalize().toAbsolutePath();
    }
}
