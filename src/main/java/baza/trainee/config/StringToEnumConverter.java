package baza.trainee.config;

import baza.trainee.enums.TypePicture;
import org.springframework.core.convert.converter.Converter;

/**
 * Class that implements {@link Converter} contract.
 * Convert sting to type picture
 * Default type ORIGINAL
 *
 * @author Andry Sitarskiy
 */
public class StringToEnumConverter implements Converter<String, TypePicture> {
    @Override
    public TypePicture convert(String source) {
        try {
            return TypePicture.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            return TypePicture.ORIGINAL;
        }
    }
}
