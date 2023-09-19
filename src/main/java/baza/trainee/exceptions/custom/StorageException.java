package baza.trainee.exceptions.custom;

import org.springframework.http.HttpStatus;

public class StorageException extends BasicApplicationException {

    /**
     * Constructs new StorageException with the specified message.
     *
     * @param message exception message.
     */
    public StorageException(final String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
