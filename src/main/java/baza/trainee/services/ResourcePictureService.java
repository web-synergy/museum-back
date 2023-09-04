package baza.trainee.services;

import org.springframework.core.io.Resource;

import java.io.IOException;

public interface ResourcePictureService {
    Resource loadAsResource(String filename) throws IOException;
}
