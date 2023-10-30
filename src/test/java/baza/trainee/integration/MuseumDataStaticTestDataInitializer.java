package baza.trainee.integration;

import baza.trainee.repository.MuseumDataRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;


@TestConfiguration
class MuseumDataStaticTestDataInitializer {

    @Bean
    CommandLineRunner testDataInitializer(MuseumDataRepository repository) {
        return args -> {
            if (!repository.findAll().isEmpty()) {
                repository.deleteAll();
            }
        };
    }
}
