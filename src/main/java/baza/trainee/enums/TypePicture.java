package baza.trainee.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * An enumeration representing different picture types.
 * Content types can be used to compression different types of picture.
 */
@AllArgsConstructor
@Getter
public enum TypePicture {
    /** This type is typically used for compression file, other than ORIGINAL.*/
    ORIGINAL(1024, 1.0f), PREVIEW(680, 0.5f);
    /**Target width compression file.*/
    private int targetWidth;
    /**Quality compression file.*/
    private float quality;


}
