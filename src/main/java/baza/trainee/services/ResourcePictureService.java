package baza.trainee.services;

import baza.trainee.domain.enums.TypePicture;

public interface ResourcePictureService {

    /**
     * Get path of type picture in upload/{type}.
     *
     * @param type     Type picture
     * @param filename file path in upload/{type}
     * @return Massive byte of picture
     */
    byte[] getPicture(TypePicture type, String filename);

    /**
     * Get picture from temp directory.
     *
     * @param userId User id
     * @param type Type picture
     * @param filename path file in temp directory
     * @return massive byte of picture*/
    byte[] getPictureFromTemp(String userId, TypePicture type,
                            String filename);
}
