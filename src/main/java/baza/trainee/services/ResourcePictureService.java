package baza.trainee.services;

import org.springframework.core.io.Resource;

public interface ResourcePictureService {
    /**
     * Load resource from upload/{type}/{filename}
     *
     * @param type Can type is original,preview or temp
     * @param filename file path in upload/{type}
     * @return Resource file*/
    Resource loadAsResource(String type, String filename);
}
