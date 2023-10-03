package baza.trainee.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ROOT("ROOT"),
    ADMIN("ADMIN");

    private final String value;

    public static Role ofStringValue(String value) {
        return switch (value) {
            case ("ROOT") -> ROOT;
            case ("ADMIT") -> ADMIN;
            default -> throw new IllegalArgumentException("Role of value '" + value + "' is not exists.");
        };
    }
}
