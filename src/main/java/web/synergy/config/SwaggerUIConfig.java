package web.synergy.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@SecuritySchemes(value = {
        @SecurityScheme(
                type = SecuritySchemeType.HTTP,
                name = "basicAuth",
                scheme = "basic"),
        @SecurityScheme(
                type = SecuritySchemeType.HTTP,
                name = "bearerAuth",
                bearerFormat = "JWT",
                scheme = "bearer"
        )})
public class SwaggerUIConfig { }

