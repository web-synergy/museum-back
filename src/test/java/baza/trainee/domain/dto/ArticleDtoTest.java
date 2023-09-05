package baza.trainee.domain.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static baza.trainee.constants.ArticleModelConstants.NOT_VALID_ARTICLE_DTO;
import static baza.trainee.constants.ArticleModelConstants.MODEL_VALIDATION_ERROR_COUNT;
import static baza.trainee.constants.ArticleModelConstants.VALID_ARTICLE_DTO;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ArticleDtoTest {

    private Validator validator;

    @BeforeAll
    void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void articleDtoValidation_Valid() {
        assertThat(validator.validate(VALID_ARTICLE_DTO)).isEmpty();
    }

    @Test
    void articleDtoValidation_threeFieldsNotValid() {
        assertThat(validator.validate(NOT_VALID_ARTICLE_DTO)).hasSize(MODEL_VALIDATION_ERROR_COUNT);
    }
}
