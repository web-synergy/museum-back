package baza.trainee.domain.model;


import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleTest {

    public static final Article VALID_ARTICLE;
    public static final Article NOT_VALID_ARTICLE;
    private Validator validator;

    static {
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
    }

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void articleValidation_Valid() {
        assertThat(validator.validate(VALID_ARTICLE)).isEmpty();
    }

    @Test
    void articleValidation_threeFieldsNotValid() {
        assertThat(validator.validate(NOT_VALID_ARTICLE)).hasSize(3);
    }
}