package web.synergy.exceptions.custom;

import org.springframework.http.HttpStatus;

public class ImageCompressionException extends BasicApplicationException {

    public ImageCompressionException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
