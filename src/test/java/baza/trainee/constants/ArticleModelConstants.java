package baza.trainee.constants;

import baza.trainee.domain.model.Article;
import baza.trainee.domain.model.ContentBlock;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

/**
 * Constant objects required for testing the 'article' business model.
 *
 * @author Olha Ryzhkova
 * @version 1.0
 */
public class ArticleModelConstants {

    public static final String GET_BY_TITLE_URL;

    public static final Article VALID_ARTICLE;
    public static final Article NOT_VALID_ARTICLE;

    public static final Integer MODEL_VALIDATION_ERROR_COUNT;

    static {
        GET_BY_TITLE_URL = "/api/articles/{title}";

        VALID_ARTICLE = Article.builder()
                .id("testId")
                .title("testTitle")
                .description("testDescription")
                .content(new HashSet<>(List.of(new ContentBlock())))
                .created(LocalDate.now())
                .updated(null)
                .build();

        NOT_VALID_ARTICLE = Article.builder()
                .id("testId")
                .title("")
                .description(" ")
                .content(new HashSet<>())
                .created(LocalDate.now())
                .updated(null)
                .build();

        MODEL_VALIDATION_ERROR_COUNT = 5;
    }
}
