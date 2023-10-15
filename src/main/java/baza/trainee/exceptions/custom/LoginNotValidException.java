package baza.trainee.exceptions.custom;

import org.springframework.http.HttpStatus;

public class LoginNotValidException extends BasicApplicationException {
    /**
     * Creates a new BasicApplicationException object
     * with the specified message and HTTP status.
     *
     * @param message    Error message explaining the reason for the exception.
     */
    public LoginNotValidException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
