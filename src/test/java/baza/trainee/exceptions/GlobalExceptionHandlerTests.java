package baza.trainee.exceptions;

import baza.trainee.exceptions.custom.BasicApplicationException;
import baza.trainee.exceptions.custom.EntityAlreadyExistsException;
import baza.trainee.exceptions.custom.EntityNotFoundException;
import baza.trainee.exceptions.custom.MethodArgumentNotValidException;
import baza.trainee.exceptions.custom.NullEntityReferenceException;
import baza.trainee.exceptions.errors.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class GlobalExceptionHandlerTests {
    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    public void testNotFoundExceptionHandling() {
        BasicApplicationException
                exception = new EntityNotFoundException("Event", "Id: 25");

        ResponseEntity<ErrorResponse> response =
                globalExceptionHandler.handleCustomException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().timestamp()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo(
                exception.getMessage());
    }

    @Test
    public void testAlreadyExistsExceptionHandling() {
        BasicApplicationException
                exception = new EntityAlreadyExistsException("Event", "Id: 25");

        ResponseEntity<ErrorResponse> response =
                globalExceptionHandler.handleCustomException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().timestamp()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo(
                exception.getMessage());
    }

    @Test
    public void testNullEntityReferenceExceptionHandling() {
        BasicApplicationException
                exception = new NullEntityReferenceException("Event");

        ResponseEntity<ErrorResponse> response =
                globalExceptionHandler.handleCustomException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().timestamp()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo(
                exception.getMessage());
    }

    @Test
    public void testMethodArgumentNotValidExceptionHandling() {
        BasicApplicationException
                exception =
                new MethodArgumentNotValidException("Invalid input params!");

        ResponseEntity<ErrorResponse> response =
                globalExceptionHandler.handleCustomException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().timestamp()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo(
                exception.getMessage());
    }

    @Test
    public void testServerExceptionHandling() {
        Exception exception =
                new NumberFormatException("Invalid path variable!");

        ResponseEntity<ErrorResponse> response =
                globalExceptionHandler.handleServerException(exception);

        assertThat(response.getStatusCode()).isEqualTo(
                HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().timestamp()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo(
                exception.getMessage());
    }
}
