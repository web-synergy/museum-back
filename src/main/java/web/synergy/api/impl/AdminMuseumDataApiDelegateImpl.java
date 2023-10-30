package web.synergy.api.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import web.synergy.api.AdminMuseumDataApiDelegate;
import web.synergy.domain.mapper.MuseumDataMapper;
import web.synergy.domain.model.MuseumData;
import web.synergy.dto.MuseumInfo;
import web.synergy.service.MuseumDataService;
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
