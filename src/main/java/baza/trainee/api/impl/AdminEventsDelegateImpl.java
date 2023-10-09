package baza.trainee.api.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import baza.trainee.api.AdminEventsApiDelegate;
import baza.trainee.dto.EventPublication;
import baza.trainee.dto.EventResponse;
import baza.trainee.service.EventService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminEventsDelegateImpl implements AdminEventsApiDelegate {

    private final EventService eventService;
    private final HttpServletRequest httpServletRequest;

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

}
