package baza.trainee.domain.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static baza.trainee.constants.MailConstants.FIELD_EMAIL_ERROR_MSG;
import static baza.trainee.constants.MailConstants.FIELD_MESSAGE_ERROR_MSG;
import static baza.trainee.constants.MailConstants.FIELD_NAME_ERROR_MSG;
import static baza.trainee.constants.MailConstants.NULL_FIELD_ERROR_MSG;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MailDtoTest {

    private static MailDto validMailDto;
    private static MailDto invalidMailDto;
    private static Validator validator;

    @BeforeAll
    public static void init() {
        validMailDto = new MailDto("John", "Doe", "test@gmail.com", "User message");
        invalidMailDto = new MailDto(" ", " ", null, "msg");
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void createValidMailDto() {
        Set<ConstraintViolation<MailDto>> violations = validator.validate(validMailDto);
        assertEquals(0, violations.size());
    }

    @Test
    void createInvalidMailDto() {
        Set<ConstraintViolation<MailDto>> violations = validator.validate(invalidMailDto);
        assertEquals(6, violations.size());
    }

    @Test
    void createMailDtoWithNullFirstName() {
        MailDto mailDto = new MailDto(null, "Doe", "test@gmail.com", "User message");
        Set<ConstraintViolation<MailDto>> violations = validator.validate(mailDto);

        assertEquals(1, violations.size());

        ConstraintViolation<MailDto> violation = violations.iterator().next();
        String actualMessage = violation.getMessage();

        assertEquals(NULL_FIELD_ERROR_MSG, actualMessage);
    }

    @Test
    void createMailDtoWithTooShortFirstName() {
        MailDto mailDto = new MailDto("J", "Doe", "test@gmail.com", "User message");
        Set<ConstraintViolation<MailDto>> violations = validator.validate(mailDto);

        assertEquals(1, violations.size());

        ConstraintViolation<MailDto> violation = violations.iterator().next();
        String actualMessage = violation.getMessage();

        assertEquals(FIELD_NAME_ERROR_MSG, actualMessage);
    }

    @Test
    void createMailDtoWithTooLongLastName() {
        MailDto mailDto = new MailDto("J".repeat(31), "Doe", "test@gmail.com", "User message");
        Set<ConstraintViolation<MailDto>> violations = validator.validate(mailDto);

        assertEquals(1, violations.size());

        ConstraintViolation<MailDto> violation = violations.iterator().next();
        String actualMessage = violation.getMessage();

        assertEquals(FIELD_NAME_ERROR_MSG, actualMessage);
    }

    @Test
    void createMailDtoWithInvalidEmail() {
        MailDto mailDto = new MailDto("John", "Doe", "testgmail.com", "User message");
        Set<ConstraintViolation<MailDto>> violations = validator.validate(mailDto);

        assertEquals(1, violations.size());

        ConstraintViolation<MailDto> violation = violations.iterator().next();
        String actualMessage = violation.getMessage();

        assertEquals(FIELD_EMAIL_ERROR_MSG, actualMessage);
    }

    @Test
    void createMailDtoWithTooShortMessage() {
        MailDto mailDto = new MailDto("John", "Doe", "test@gmail.com", "msg");
        Set<ConstraintViolation<MailDto>> violations = validator.validate(mailDto);

        assertEquals(1, violations.size());

        ConstraintViolation<MailDto> violation = violations.iterator().next();
        String actualMessage = violation.getMessage();

        assertEquals(FIELD_MESSAGE_ERROR_MSG, actualMessage);
    }

    @Test
    void createMailDtoWithTooLongMessage() {
        MailDto mailDto = new MailDto("John", "Doe", "test@gmail.com", "message".repeat(99));
        Set<ConstraintViolation<MailDto>> violations = validator.validate(mailDto);

        assertEquals(1, violations.size());

        ConstraintViolation<MailDto> violation = violations.iterator().next();
        String actualMessage = violation.getMessage();

        assertEquals(FIELD_MESSAGE_ERROR_MSG, actualMessage);
    }
}
