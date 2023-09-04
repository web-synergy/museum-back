package baza.trainee.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Value("${uploads.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        /* Path: localhost:8080/img/2023/9/look.jpg refers to the directory:
        absolute path to the project + /uploads + 2023/9(own directory) +
        look.jpg(own file) */
        registry.addResourceHandler("/img/**")
                .addResourceLocations(
                        "file:///" + System.getProperty("user.dir") +
                                uploadPath + "/");
    }
}
