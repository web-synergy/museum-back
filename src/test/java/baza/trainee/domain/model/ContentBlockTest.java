package baza.trainee.domain.model;

import baza.trainee.domain.enums.BlockType;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ContentBlockTest {

    private Validator validator;
    private ContentBlock validBlock;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        validBlock = new ContentBlock();
        validBlock.setId("12");
        validBlock.setBlockType(BlockType.PICTURE_TEXT_BLOCK);
        validBlock.setOrder(2);
        validBlock.setColumns(4);
        validBlock.setPictureLink("https://example.com/banner.jpg");
        validBlock.setTextContent("text content");
    }

    @Test
    public void testContentBlockWithValidData() {
        // given:
        ContentBlock contentBlock = validBlock;

        // then:
        assertTrue(validator.validate(contentBlock).isEmpty());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(ints = {-1})
    public void testEventWithInvalidOrder(Integer order) {
        // given:
        ContentBlock contentBlock = new ContentBlock();
        contentBlock.setOrder(order);

        // then:
        assertFalse(validator.validate(contentBlock).isEmpty());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(ints = {-1})
    public void testEventWithInvalidColumns(Integer columns) {
        // given:
        ContentBlock contentBlock = new ContentBlock();
        contentBlock.setColumns(columns);

        // then:
        assertFalse(validator.validate(contentBlock).isEmpty());
    }

    @ParameterizedTest
    @NullSource
    public void testEventWithInvalidColumns(BlockType type) {
        // given:
        ContentBlock contentBlock = new ContentBlock();
        contentBlock.setBlockType(type);

        // then:
        assertFalse(validator.validate(contentBlock).isEmpty());
    }
}
