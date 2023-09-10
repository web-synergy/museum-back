package baza.trainee.constants;

import baza.trainee.domain.dto.ArticleDto;
import baza.trainee.domain.model.Article;

import java.time.LocalDate;
import java.util.HashSet;

/**
 * Constant objects required for testing the 'article' business model.
 *
 * @author Olha Ryzhkova
 * @version 1.0
 */
public class ArticleModelConstants {

    public static final String GET_BY_ID_URL;

    public static final ArticleDto VALID_ARTICLE_DTO;
    public static final ArticleDto NOT_VALID_ARTICLE_DTO;

    public static final Article VALID_ARTICLE;
    public static final Article NOT_VALID_ARTICLE;

    public static final Integer MODEL_VALIDATION_ERROR_COUNT;

    static {
        GET_BY_ID_URL = "/api/article/{articleId}";

        VALID_ARTICLE_DTO = new ArticleDto("testId", "testTitle", "testDescription",
                "testContent", new HashSet<>(), LocalDate.now(),null);

        NOT_VALID_ARTICLE_DTO = new ArticleDto("testId", "", " ", null,
                new HashSet<>(), LocalDate.now(), null);

        VALID_ARTICLE = Article.builder()
                .id("testId")
                .title("testTitle")
                .description("testDescription")
                .content("testContent")
                .images(new HashSet<>())
                .created(LocalDate.now())
                .updated(null)
                .build();

        NOT_VALID_ARTICLE = Article.builder()
                .id("testId")
                .title("")
                .description(" ")
                .content(null)
                .images(new HashSet<>())
                .created(LocalDate.now())
                .updated(null)
                .build();

        MODEL_VALIDATION_ERROR_COUNT = 3;
    }
}
