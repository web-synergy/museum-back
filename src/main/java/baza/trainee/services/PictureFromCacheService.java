package baza.trainee.services;

import java.util.List;

public interface PictureFromCacheService {
    boolean addPicture(String shortPathPicture);

    boolean addPictures(List<String> shortPathsPicture);

    boolean deletePicture(String shortPathPicture);

    boolean deletePictures(List<String> shortPathsPicture);
}
