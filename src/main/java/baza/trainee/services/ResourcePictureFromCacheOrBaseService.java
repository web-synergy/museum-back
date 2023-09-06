package baza.trainee.services;

import org.springframework.core.io.Resource;

public interface ResourcePictureFromCacheOrBaseService {
    Resource loadAsResource(String filename);
}
