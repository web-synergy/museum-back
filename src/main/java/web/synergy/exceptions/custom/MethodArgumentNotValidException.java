package web.synergy.exceptions.custom;

import org.springframework.http.HttpStatus;

public class MethodArgumentNotValidException extends BasicApplicationException {

    /**
     * Constructs MethodArgumentNotValidException with the specified error message.
     *
     * @param message Error message explaining the reason for the exception.
     */
    public MethodArgumentNotValidException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
