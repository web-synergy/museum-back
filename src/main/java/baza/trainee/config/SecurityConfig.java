package baza.trainee.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import java.util.Arrays;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

import lombok.RequiredArgsConstructor;

/**
 * SecurityConfig handles the security configurations.
 *
 * @author Evhen Malysh
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${jwt.key}")
    private String jwtKey;

    @Value("${web.cors.allowed_origins}")
    private String[] allowedOrigins;

    @Value("${web.cors.allowed_methods}")
    private String[] allowedMethods;

    @Value("${web.cors.max_age}")
    private long maxAge;

    @Value("${web.cors.allowed_headers}")
    private String[] allowedHeaders;

    @Value("${web.cors.exposed_headers}")
    private String[] exposedHeaders;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedOrigin(allowedOrigins[0]);
        config.setAllowedMethods(Arrays.asList(allowedMethods));
        config.setAllowedHeaders(Arrays.asList(allowedHeaders));
        config.setExposedHeaders(Arrays.asList(exposedHeaders));
        config.setMaxAge(maxAge);
        config.setAllowCredentials(true);

        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cc -> cc.configurationSource(corsFilter()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/api/user/register").permitAll()
                        .requestMatchers("/api/admin/update/recovery-password").permitAll()
                        .requestMatchers("/api/admin/login").hasAnyRole("ADMIN", "ROOT")
                        .requestMatchers("/api/admin/**").hasAuthority("SCOPE_WRITE")
                        .requestMatchers("/api/**").permitAll())
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .oauth2ResourceServer(t -> t.jwt(Customizer.withDefaults()))
                .httpBasic(Customizer.withDefaults())
                .logout(flc -> flc.logoutUrl("/api/admin/auth/logout"))
                .build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(jwtKey.getBytes()));
    }

    @Bean
    JwtDecoder jwtDecoder() {
        byte[] bytes = jwtKey.getBytes();
        var originalKey = new SecretKeySpec(bytes, 0, bytes.length, "RSA");
        return NimbusJwtDecoder
                .withSecretKey(originalKey)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

}
