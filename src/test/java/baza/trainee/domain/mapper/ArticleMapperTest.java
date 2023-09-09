package baza.trainee.domain.mapper;

import baza.trainee.domain.dto.ArticleDto;
import baza.trainee.domain.model.Article;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static baza.trainee.constants.ArticleModelConstants.VALID_ARTICLE;
import static baza.trainee.constants.ArticleModelConstants.VALID_ARTICLE_DTO;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {ArticleMapperImpl.class})
class ArticleMapperTest {

    @Autowired
    ArticleMapperImpl articleMapper;

    @Test
    void toArticleTest() {
        final Article result = articleMapper.toArticle(VALID_ARTICLE_DTO);

        assertEquals(result.getId(), VALID_ARTICLE_DTO.id());
        assertEquals(result.getTitle(), VALID_ARTICLE_DTO.title());
        assertEquals(result.getDescription(), VALID_ARTICLE_DTO.description());
        assertEquals(result.getContent(), VALID_ARTICLE_DTO.content());
        assertEquals(result.getImages(), VALID_ARTICLE_DTO.images());
        assertEquals(result.getCreated(), VALID_ARTICLE_DTO.created());
        assertEquals(result.getUpdated(), VALID_ARTICLE_DTO.updated());
    }

    @Test
    void toDtoTest() {
        final ArticleDto result = articleMapper.toDto(VALID_ARTICLE);

        assertEquals(result.id(), VALID_ARTICLE.getId());
        assertEquals(result.title(), VALID_ARTICLE.getTitle());
        assertEquals(result.description(), VALID_ARTICLE.getDescription());
        assertEquals(result.content(), VALID_ARTICLE.getContent());
        assertEquals(result.images(), VALID_ARTICLE.getImages());
        assertEquals(result.created(), VALID_ARTICLE.getCreated());
        assertEquals(result.updated(), VALID_ARTICLE.getUpdated());
    }
}
