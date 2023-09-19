package baza.trainee.integration;

import baza.trainee.service.ArticleService;
import baza.trainee.service.EventService;
import baza.trainee.service.MailService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class MockConfiguration {

    @Bean
    ArticleService articleService() {
        return mock(ArticleService.class);
    }

    @Bean
    EventService eventService() {
        return mock(EventService.class);
    }

    @Bean
    MailService mailService() {
        return mock(MailService.class);
    }

}
