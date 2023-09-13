package baza.trainee.integration;

import baza.trainee.service.ArticleService;
import baza.trainee.service.MailService;
import baza.trainee.service.SearchService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class EventServiceMockConfig {

    @Bean
    ArticleService articleService() {
        return mock(ArticleService.class);
    }

    @Bean
    MailService mailService() {
        return mock(MailService.class);
    }

    @Bean
    SearchService searchService() {
        return mock(SearchService.class);
    }

}
