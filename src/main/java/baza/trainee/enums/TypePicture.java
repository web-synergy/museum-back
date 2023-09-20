package baza.trainee.enums;

import baza.trainee.services.PictureTempService;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * An enumeration representing different picture types.
 * Content types can be used to compression different types of picture.
 */
@AllArgsConstructor
@Getter
public enum TypePicture {ORIGINAL(1024, 1.0f),PREVIEW(680, 0.5f);
    private int targetWidth;
    private float quality;


}
