package baza.trainee.exceptions;

import baza.trainee.exceptions.custom.*;
import baza.trainee.exceptions.errors.ErrorResponse;
import baza.trainee.security.RootUserInitializer;
import baza.trainee.service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GlobalExceptionHandlerTests {

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @MockBean
    private EventService eventService;

    @MockBean
    private RootUserInitializer rootUserInitializer;

    @Test
    void testNotFoundExceptionHandling() {
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
    void testAlreadyExistsExceptionHandling() {
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
    void testNullEntityReferenceExceptionHandling() {
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
    void testMethodArgumentNotValidExceptionHandling() {
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
    void testServerExceptionHandling() {
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
