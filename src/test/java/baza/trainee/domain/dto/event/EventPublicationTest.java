package baza.trainee.domain.dto.event;

import baza.trainee.domain.model.ContentBlock;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EventPublicationTest {

    private Validator validator;
    private EventPublication validEventPublication;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        validEventPublication = new EventPublication(
                "Title",
                "Short Description",
                "PAINTING",
                Set.of("tag1", "tag2"),
                Set.of(new ContentBlock()),
                "http://example.com/banner.jpg",
                LocalDate.now(),
                LocalDate.now().plusDays(1));
    }

    @Test
    void testValidEventDto() {
        // given:
        EventPublication eventPublication = validEventPublication;

        // when:
        var constraintViolations = validator.validate(eventPublication);

        // then:
        assertTrue(constraintViolations.isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testInvalidTitle(String title) {
        // given:
        EventPublication eventPublication = new EventPublication(
                title,
                validEventPublication.description(),
                validEventPublication.type(),
                validEventPublication.tags(),
                validEventPublication.content(),
                validEventPublication.bannerTempURI(),
                validEventPublication.begin(),
                validEventPublication.end()
        );

        // when:
        var constraintViolations = validator.validate(eventPublication);

        // then:
        assertFalse(constraintViolations.isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testInvalidDescription(String description) {
        // given:
        EventPublication eventPublication = new EventPublication(
                validEventPublication.title(),
                description,
                validEventPublication.type(),
                validEventPublication.tags(),
                validEventPublication.content(),
                validEventPublication.bannerTempURI(),
                validEventPublication.begin(),
                validEventPublication.end()
        );

        // when:
        var constraintViolations = validator.validate(eventPublication);

        // then:
        assertFalse(constraintViolations.isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testInvalidType(String type) {
        // given:
        EventPublication eventPublication = new EventPublication(
                validEventPublication.title(),
                validEventPublication.description(),
                type,
                validEventPublication.tags(),
                validEventPublication.content(),
                validEventPublication.bannerTempURI(),
                validEventPublication.begin(),
                validEventPublication.end()
        );

        // when:
        var constraintViolations = validator.validate(eventPublication);

        // then:
        assertFalse(constraintViolations.isEmpty());
    }

    @ParameterizedTest
    @MethodSource
    void testInvalidDates(LocalDate date) {
        // given:
        EventPublication eventPublication = new EventPublication(
                validEventPublication.title(),
                validEventPublication.description(),
                validEventPublication.type(),
                validEventPublication.tags(),
                validEventPublication.content(),
                validEventPublication.bannerTempURI(),
                date,
                date
        );

        // when:
        var constraintViolations = validator.validate(eventPublication);

        // then:
        assertFalse(constraintViolations.isEmpty());
    }

    public static Stream<Arguments> testInvalidDates() {
        return Stream.of(
                Arguments.of(LocalDate.now().minusDays(1))
        );
    }

    @Test
    void testBeginDateAfterEndDate() {
        // expected:
        assertThrows(
                IllegalArgumentException.class,
                () -> new EventPublication(
                        validEventPublication.title(),
                        validEventPublication.description(),
                        validEventPublication.type(),
                        validEventPublication.tags(),
                        validEventPublication.content(),
                        validEventPublication.bannerTempURI(),
                        LocalDate.now().plusDays(2),
                        LocalDate.now()));
    }
}
