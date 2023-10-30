package web.synergy.exceptions;

import web.synergy.exceptions.custom.BasicApplicationException;
import web.synergy.exceptions.errors.ErrorResponse;
import web.synergy.utils.Logger;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles custom application exceptions and logs the error
     * before returning an error response.
     *
     * @param ex The custom application exception to handle.
     * @return A ResponseEntity containing an error response
     * with the exception message and timestamp.
     */
    @ExceptionHandler(BasicApplicationException.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Entity not found")
    })
    public ResponseEntity<ErrorResponse> handleCustomException(final BasicApplicationException ex) {
        Logger.error(ex.getClass().getSimpleName(), ex.getMessage());

        ErrorResponse response = new ErrorResponse(ex.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, ex.getHttpStatus());
    }

    /**
     * Handles {@link UsernameNotFoundException} exception and logs the error
     * before returning an error response 404.
     *
     * @param ex {@link UsernameNotFoundException} exception to handle.
     * @return A ResponseEntity containing an error response 404
     * with the exception message and timestamp.
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    @ApiResponse(responseCode = "404", description = "Entity not found")
    public ResponseEntity<ErrorResponse> handleNotFoundException(final UsernameNotFoundException ex) {
        Logger.error(ex.getClass().getSimpleName(), ex.getMessage());

        ErrorResponse response = new ErrorResponse(ex.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles {@link MethodArgumentNotValidException} and logs the error
     * before returning an status 400.
     *
     * @param ex The {@link MethodArgumentNotValidException} exception to handle.
     * @return A ResponseEntity containing an error response
     * with the exception message and timestamp.
     */
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            MissingServletRequestParameterException.class,
            ConstraintViolationException.class})
    @ApiResponse(responseCode = "400", description = "Invalid Input")
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidExceptionException(final Exception ex) {
        Logger.error(ex.getClass().getSimpleName(), ex.getMessage());

        ErrorResponse response = new ErrorResponse(ex.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles server exceptions and logs the error
     * before returning an internal server error response.
     *
     * @param ex The server exception to handle.
     * @return A ResponseEntity containing an error response
     * with the exception message and timestamp.
     */
    @ExceptionHandler(Exception.class)
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public ResponseEntity<ErrorResponse> handleServerException(final Exception ex) {
        Logger.error(ex.getClass().getSimpleName(), ex.getMessage());

        ErrorResponse response = new ErrorResponse(ex.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
