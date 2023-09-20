package baza.trainee.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TypePicture {ORIGINAL(1024, 1.0f),PREVIEW(680, 0.5f);
    private int targetWidth;
    private float quality;


}
