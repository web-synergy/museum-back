package web.synergy.api.impl;

import web.synergy.api.ImagesApiDelegate;
import web.synergy.service.ImageService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageApiDelegateImpl implements ImagesApiDelegate {

    private final ImageService imageService;

    @Override
    public ResponseEntity<byte[]> getImage(String filename, String type) {
        return new ResponseEntity<>(imageService.loadResource(filename, type), HttpStatus.OK);
    }
}
