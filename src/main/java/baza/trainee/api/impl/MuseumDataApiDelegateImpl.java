package baza.trainee.api.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import baza.trainee.api.MuseumDataApiDelegate;
import baza.trainee.domain.model.MuseumData;
import baza.trainee.service.MuseumDataService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MuseumDataApiDelegateImpl implements MuseumDataApiDelegate {

    private final MuseumDataService museumDataService;
    
    @Override
    public ResponseEntity<MuseumData> getMuseumData() {
        return new ResponseEntity<>(museumDataService.getData(), HttpStatus.OK);
    }
}
