package baza.trainee.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import static baza.trainee.constants.MailConstants.FIELD_EMAIL_ERROR_MSG;
import static baza.trainee.constants.MailConstants.FIELD_MESSAGE_ERROR_MSG;
import static baza.trainee.constants.MailConstants.FIELD_NAME_ERROR_MSG;
import static baza.trainee.constants.MailConstants.MAX_MSG_LENGTH;
import static baza.trainee.constants.MailConstants.MAX_NAME_LENGTH;
import static baza.trainee.constants.MailConstants.MIN_MSG_LENGTH;
import static baza.trainee.constants.MailConstants.MIN_NAME_LENGTH;
import static baza.trainee.constants.MailConstants.NULL_FIELD_ERROR_MSG;

public record MailDto(
        @NotBlank(message = NULL_FIELD_ERROR_MSG)
        @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH, message = FIELD_NAME_ERROR_MSG)
        String firstName,

        @NotBlank(message = NULL_FIELD_ERROR_MSG)
        @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH, message = FIELD_NAME_ERROR_MSG)
        String lastName,

        @NotBlank(message = NULL_FIELD_ERROR_MSG)
        @Email(message = FIELD_EMAIL_ERROR_MSG)
        String email,

        @NotBlank(message = NULL_FIELD_ERROR_MSG)
        @Size(min = MIN_MSG_LENGTH, max = MAX_MSG_LENGTH, message = FIELD_MESSAGE_ERROR_MSG)
        String message
) {
}
