package baza.trainee.utils;

import baza.trainee.exceptions.custom.MethodArgumentNotValidException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

public class ControllerUtils {

    /**
     * Handles field validation errors and
     * throws a MethodArgumentNotValidException if errors are present.
     *
     * @param bindingResult The BindingResult containing field validation errors.
     * @throws MethodArgumentNotValidException if there are field validation errors.
     */
    public static void handleFieldsErrors(final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            errors.forEach(err -> errorMessage.append(err.getField())
                    .append(" - ").append(err.getDefaultMessage())
                    .append(";"));

            throw new MethodArgumentNotValidException(errorMessage.toString());
        }
    }
}
