package baza.trainee.domain.enums;

import lombok.Getter;

@Getter
public enum BlockType {
    TEXT_BLOCK("text-block"),
    PICTURE_BLOCK("picture-block"),
    PICTURE_TEXT_BLOCK("picture-text-block");

    private final String value;

    BlockType(String value) {
        this.value = value;
    }
}
