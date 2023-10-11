package baza.trainee.api.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import baza.trainee.api.AdminMuseumDataApiDelegate;
import baza.trainee.domain.mapper.MuseumDataMapper;
import baza.trainee.domain.model.MuseumData;
import baza.trainee.dto.MuseumInfo;
import baza.trainee.service.MuseumDataService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminMuseumDataApiDelegateImpl implements AdminMuseumDataApiDelegate {

    private final MuseumDataService museumDataService;
    private final MuseumDataMapper museumDataMapper;



    @Override
    public ResponseEntity<MuseumData> addData(MuseumInfo museumInfo) {
        var museumData = museumDataMapper.toMuseumData(museumInfo);
        return new ResponseEntity<>(museumDataService.add(museumData),
                HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<MuseumData> updateData(MuseumInfo museumInfo) {
        var museumData = museumDataMapper.toMuseumData(museumInfo);
        return new ResponseEntity<>(museumDataService.update(museumData),
                HttpStatus.OK);
    }
    
}
