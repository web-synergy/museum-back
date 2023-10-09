package baza.trainee.api.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import baza.trainee.api.AdminImagesApiDelegate;
import baza.trainee.dto.SaveImageResponse;
import baza.trainee.service.ImageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminImageApiDelegateImpl implements AdminImagesApiDelegate {

    private final ImageService imageService;
    private final HttpServletRequest httpServletRequest;

    @Override
    public ResponseEntity<SaveImageResponse> saveImage(MultipartFile file) {
        var sessionId = httpServletRequest.getSession().getId();
        return new ResponseEntity<>(
                imageService.storeToTemp(file, sessionId),
                HttpStatus.CREATED);
    }
    
}
