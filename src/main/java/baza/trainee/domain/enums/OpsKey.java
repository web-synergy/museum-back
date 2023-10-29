package baza.trainee.domain.enums;

import lombok.Getter;

@Getter
public enum OpsKey {
    EMAIL_KEY("EMAIL_KEY"),
    CONFIRM_CODE_KEY("CONFIRM_CODE_KEY");

    private final String value;

    OpsKey(String value) {
        this.value = value;
    }
}