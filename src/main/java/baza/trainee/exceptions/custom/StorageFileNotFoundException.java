package baza.trainee.exceptions.custom;

import org.springframework.http.HttpStatus;

public class StorageFileNotFoundException extends BasicApplicationException {

    /**
     * Constructs new StorageFileNotFoundException with the specified message.
     *
     * @param message exception message.
     */
    public StorageFileNotFoundException(final String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
