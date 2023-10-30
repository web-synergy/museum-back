package web.synergy.utils;

import java.util.function.Supplier;

import web.synergy.exceptions.custom.EntityNotFoundException;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ExceptionUtils {
    

    public static Supplier<EntityNotFoundException> getNotFoundExceptionSupplier(Class<?> clazz, String details) {
        return () -> new EntityNotFoundException(clazz.getSimpleName(), details);
    }
}
