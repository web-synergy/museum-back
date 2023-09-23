package baza.trainee.config;

import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer {
    /**Registry converter.*/
    @Override
    public void addFormatters(final FormatterRegistry registry) {
        registry.addConverter(new StringToEnumConverter());
    }
}
