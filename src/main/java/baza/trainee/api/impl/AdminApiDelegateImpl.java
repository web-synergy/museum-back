package baza.trainee.api.impl;

import baza.trainee.api.AdminApiDelegate;
import baza.trainee.domain.mapper.MuseumDataMapper;
import baza.trainee.domain.model.MuseumData;
import baza.trainee.dto.EventPublication;
import baza.trainee.dto.EventResponse;
import baza.trainee.dto.MuseumInfo;
import baza.trainee.dto.SaveImageResponse;
import baza.trainee.service.EventService;
import baza.trainee.service.ImageService;
import baza.trainee.service.MuseumDataService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AdminApiDelegateImpl implements AdminApiDelegate {

    private final EventService eventService;
    private final ImageService imageService;
    private final HttpServletRequest httpServletRequest;
    private final MuseumDataService museumDataService;
    private final MuseumDataMapper museumDataMapper;

    @Override
    public ResponseEntity<EventResponse> createEvent(EventPublication eventPublication) {
        var sessionId = httpServletRequest.getSession().getId();
        return new ResponseEntity<>(
                eventService.save(eventPublication, sessionId),
                HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<EventResponse> updateEvent(String id, EventPublication eventPublication) {
        var sessionId = httpServletRequest.getSession().getId();
        return new ResponseEntity<>(
                eventService.update(id, eventPublication, sessionId),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteEvent(String id) {
        eventService.deleteEventById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<byte[]> getTempImage(String filename, String type) {
        var sessionId = httpServletRequest.getSession().getId();
        return new ResponseEntity<>(
                imageService.loadTempResource(filename, sessionId, type),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SaveImageResponse> saveImage(MultipartFile file) {
        var sessionId = httpServletRequest.getSession().getId();
        return new ResponseEntity<>(
                imageService.storeToTemp(file, sessionId),
                HttpStatus.CREATED);
    }

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
