package baza.trainee.integration;

import baza.trainee.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ArticleServiceImplTest extends AbstractIntegrationTest {

    @Autowired
    private ArticleService articleService;

    @Test
    void saveStaticArticles() {
        var result = articleService.saveStaticArticles();

        assertThat(result)
                .isNotNull()
                .hasSize(6);

        assertEquals(1, result.stream().filter(a -> "Історія музею".equals(a.getTitle())).count());
        assertEquals(1, result.stream().filter(a -> "Митець".equals(a.getTitle())).count());
        assertEquals(1, result.stream().filter(a -> "Біографія".equals(a.getTitle())).count());
        assertEquals(1, result.stream().filter(a -> "Кіно".equals(a.getTitle())).count());
        assertEquals(1, result.stream().filter(a -> "Скульптура".equals(a.getTitle())).count());
        assertEquals(1, result.stream().filter(a -> "Митець і Київ".equals(a.getTitle())).count());
    }
}
