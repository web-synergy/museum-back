package baza.trainee.exceptions;

public class StorageException extends RuntimeException {
    /**
     * Use create storage exception.
     * @param message Message*/
    public StorageException(final String message) {
        super(message);
    }

    /**
     * Use create storage exception.
     * @param message Message
     * @param cause Cause*/
    public StorageException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
