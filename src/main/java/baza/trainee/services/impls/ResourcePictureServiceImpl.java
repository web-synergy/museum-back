package baza.trainee.services.impls;

import baza.trainee.domain.enums.TypePicture;
import baza.trainee.exceptions.StorageFileNotFoundException;
import baza.trainee.services.ResourcePictureService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

@Service
public class ResourcePictureServiceImpl implements ResourcePictureService {
    /**
     * Name root directory.
     */
    @Value("${uploads.path}")
    private String uploadPath;

    /**
     * Load resource from upload/{type}/{filename}.
     *
     * @param path Absolute path picture
     * @return Massive byte of picture
     */

    private byte[] loadAsResource(final Path path) {
        try {
            var resource = new UrlResource(path.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource.getContentAsByteArray();
            } else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + path);

            }
        } catch (IOException e) {
            throw new StorageFileNotFoundException(
                    "Could not read file: " + path, e);
        }
    }

    /**
     * Get massive byte of picture in upload/{type}.
     *
     * @param type     Type picture
     * @param filename file path in upload/{type}
     * @return Massive byte of picture
     */
    public byte[] getPicture(final TypePicture type, final String filename) {
        Path path = Path.of(System.getProperty("user.dir"), uploadPath,
                        type.name().toLowerCase(),
                        getFilenameForType(type, filename))
                .normalize().toAbsolutePath();
        return loadAsResource(path);
    }

    /**
     * Get picture from temp directory.
     *
     * @param userId   User id
     * @param type     Type picture
     * @param filename original filename
     * @return Full path from massive
     */
    @Override
    public byte[] getPictureFromTemp(final String userId,
                                     final TypePicture type,
                                     final String filename) {
        Path path = Path.of(System.getProperty("user.dir"), uploadPath,
                        userId, type.name().toLowerCase(),
                        getFilenameForType(type, filename))
                .normalize().toAbsolutePath();
        return loadAsResource(path);
    }

    private String getFilenameForType(final TypePicture type,
                                      final String filename) {
        String filenameForType;
        if (type != TypePicture.ORIGINAL) {
            filenameForType = filename.replaceFirst("\\..+$", ".webp");
        } else {
            filenameForType = filename;
        }
        return filenameForType;
    }

}
