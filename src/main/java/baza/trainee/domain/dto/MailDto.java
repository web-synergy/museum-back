package baza.trainee.domain.dto;

public record MailDto(
        String firstName,
        String lastName,
        String email,
        String message
) {
}
