package web.synergy.integration;

import web.synergy.domain.model.Article;
import web.synergy.repository.ArticleRepository;
import web.synergy.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Import({ArticleTestDataInitializer.class})
class ArticleServiceImplTest extends AbstractIntegrationTest {

    @Autowired
    private ArticleService articleService;

    @Autowired
    ArticleRepository repository;

    @Test
    void saveStaticArticles() {
        List<Article> result = repository.findAll();
        assertThat(result)
                .isNotNull()
                .isEmpty();

        articleService.saveStaticArticles();

        result = repository.findAll();
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
