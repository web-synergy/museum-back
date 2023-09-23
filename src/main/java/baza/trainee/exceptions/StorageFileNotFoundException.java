package baza.trainee.exceptions;

public class StorageFileNotFoundException extends StorageException {
    /**
     * Use create storage exception.
     * @param message Message*/
    public StorageFileNotFoundException(final String message) {
        super(message);
    }

    /**
     * Use create storage exception.
     * @param message Message
     * @param cause Cause*/
    public StorageFileNotFoundException(final String message,
                                        final Throwable cause) {
        super(message, cause);
    }
}
