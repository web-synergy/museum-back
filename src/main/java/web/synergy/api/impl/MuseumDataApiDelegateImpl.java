package web.synergy.api.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import web.synergy.api.MuseumDataApiDelegate;
import web.synergy.domain.model.MuseumData;
import web.synergy.service.MuseumDataService;
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
