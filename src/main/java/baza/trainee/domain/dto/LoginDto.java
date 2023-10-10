package baza.trainee.domain.dto;

import jakarta.validation.constraints.Email;

public record LoginDto(
        @Email
        String oldLogin,
        @Email
        String newLogin,
        @Email
        String duplicateNewLogin
) {
}
