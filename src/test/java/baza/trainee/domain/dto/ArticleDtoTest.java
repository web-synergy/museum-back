package baza.trainee.domain.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleDtoTest {

    public static final ArticleDto VALID_ARTICLE_DTO;
    public static final ArticleDto NOT_VALID_ARTICLE_DTO;
    private Validator validator;

    static {
        VALID_ARTICLE_DTO = new ArticleDto("testId", "testTitle", "testDescription",
                "testContent", new HashSet<>(), LocalDate.now(),null);
        NOT_VALID_ARTICLE_DTO = new ArticleDto("testId", "", " ", null,
                new HashSet<>(), LocalDate.now(), null);
    }

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void articleDtoValidation_Valid() {
        assertThat(validator.validate(VALID_ARTICLE_DTO)).isEmpty();
    }

    @Test
    void articleDtoValidation_threeFieldsNotValid() {
        assertThat(validator.validate(NOT_VALID_ARTICLE_DTO)).hasSize(3);
    }
}
