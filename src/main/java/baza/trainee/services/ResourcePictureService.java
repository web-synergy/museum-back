package baza.trainee.services;

import baza.trainee.enums.TypePicture;
import org.springframework.core.io.Resource;

public interface ResourcePictureService {
    /**
     * Load resource from upload/{type}/{filename}
     *
     * @param type Can type is original,preview or temp
     * @param filename file path in upload/{type}
     * @return Resource file*/
    byte[] loadAsResource(String type, String filename);
}
