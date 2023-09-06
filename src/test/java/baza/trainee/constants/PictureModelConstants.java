package baza.trainee.constants;

import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.net.MalformedURLException;
import java.nio.file.Path;


public class PictureModelConstants {
    public static final Resource VALID_IMG;

    static {
        Resource img;
        try {
            img = new UrlResource(Path.of(System.getProperty("user.dir"),
                    "src/test/java/baza/trainee/noImages.jpg").toUri());
        } catch (MalformedURLException e) {
            img=null;
        }
        VALID_IMG = img;
    }
}
