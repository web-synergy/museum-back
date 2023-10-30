package web.synergy.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class StorageProperties {

    @Value("${resources.files.root}")
    private String rootImageLocation = "files";

    @Value("${resources.files.images.root}")
    private String persistImageLocation = "images";

    @Value("${resources.files.images.original}")
    private String originalImagesLocation = "original";

    @Value("${resources.files.images.compressed}")
    private String compressedImagesLocation = "compressed";

    @Value("${resources.files.images.temporary}")
    private String tempImagesLocation = "temp";

}
