package baza.trainee.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
public class ImageCompressionConfig {

    @Value("${images.preview.width}")
    private Integer previewWidth = 640;

    @Value("${images.preview.quality}")
    private Float previewQuality = 0.5F;

    @Value("${images.desktop.width}")
    private Integer desktopWidth = 1024;

    @Value("${images.desktop.quality}")
    private Float desktopQuality = 1F;

}
