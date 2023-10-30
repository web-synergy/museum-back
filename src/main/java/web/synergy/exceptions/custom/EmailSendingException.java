package web.synergy.exceptions.custom;

import org.springframework.http.HttpStatus;

public class EmailSendingException extends BasicApplicationException {

    /**
     * Constructs EmailSendingException with the specified error message and HTTP status.
     *
     * @param message Error message explaining the reason for the exception.
     */
    public EmailSendingException(final String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
