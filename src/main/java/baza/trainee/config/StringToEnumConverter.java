package baza.trainee.config;

import baza.trainee.enums.TypePicture;
import org.springframework.core.convert.converter.Converter;

public class StringToEnumConverter implements Converter<String, TypePicture> {
    @Override
    public TypePicture convert(String source) {
        try {
            return TypePicture.valueOf(source);
        } catch (IllegalArgumentException e) {
            return TypePicture.ORIGINAL;
        }
    }
}
