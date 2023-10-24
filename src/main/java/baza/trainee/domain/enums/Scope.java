package baza.trainee.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Scope {
    READ("READ"),
    WRITE("WRITE");

    private final String value;

    public static Scope ofValueString(String value) {
        return switch (value) {
            case ("READ") -> READ;
            case ("WRITE") -> WRITE;
            default -> throw new IllegalArgumentException("Scope of value '" + value + "' is not exists.");
        };
    }
}
