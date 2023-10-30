package web.synergy.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ROOT("ROOT"),
    ADMIN("ADMIN");

    private final String value;
}
