package web.synergy.exceptions.custom;

import org.springframework.http.HttpStatus;

public class NullEntityReferenceException extends BasicApplicationException {

    /**
     * Constructs new NullEntityReferenceException with the specified entity type.
     *
     * @param entityType Type of entity for which a null reference is not allowed.
     */
    public NullEntityReferenceException(final String entityType) {
        super(String.format("%s can`nt be null!", entityType),
                HttpStatus.BAD_REQUEST);
    }
}
