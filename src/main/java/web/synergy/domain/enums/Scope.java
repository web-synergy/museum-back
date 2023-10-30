package web.synergy.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Scope {
    READ("READ"),
    WRITE("WRITE");

    private final String value;
}
