package baza.trainee.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import static baza.trainee.constants.MailConstants.MAX_MSG_LENGTH;
import static baza.trainee.constants.MailConstants.MAX_NAME_LENGTH;
import static baza.trainee.constants.MailConstants.MIN_MSG_LENGTH;
import static baza.trainee.constants.MailConstants.MIN_NAME_LENGTH;

public record MailDto(
        @NotBlank
        @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH)
        String firstName,

        @NotBlank
        @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH)
        String lastName,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = MIN_MSG_LENGTH, max = MAX_MSG_LENGTH)
        String message
) {
}
