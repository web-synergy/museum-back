package baza.trainee.api.impl;

import baza.trainee.api.ImagesApiDelegate;
import baza.trainee.service.ImageService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageApiDelegateImpl implements ImagesApiDelegate {

    private final ImageService storageService;

    @Override
    public ResponseEntity<byte[]> getImage(String filename, String type) {
        return new ResponseEntity<>(storageService.loadResource(filename, type), HttpStatus.OK);
    }
}
