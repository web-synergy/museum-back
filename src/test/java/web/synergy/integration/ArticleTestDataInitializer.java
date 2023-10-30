package web.synergy.integration;

import web.synergy.repository.ArticleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;


@TestConfiguration
class ArticleTestDataInitializer {

    @Bean
    CommandLineRunner testDataInitializer(ArticleRepository repository) {
        return args -> {
            if (!repository.findAll().isEmpty()) {
                repository.deleteAll();
            }
        };
    }
}
