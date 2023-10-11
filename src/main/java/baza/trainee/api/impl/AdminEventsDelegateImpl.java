package baza.trainee.api.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import baza.trainee.api.AdminEventsApiDelegate;
import baza.trainee.dto.EventPublication;
import baza.trainee.dto.EventResponse;
import baza.trainee.service.EventService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminEventsDelegateImpl implements AdminEventsApiDelegate {

    private final EventService eventService;

    @Override
    public ResponseEntity<EventResponse> createEvent(EventPublication eventPublication) {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity<>(
                eventService.save(eventPublication, username),
                HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<EventResponse> updateEvent(String id, EventPublication eventPublication) {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity<>(
                eventService.update(id, eventPublication, username),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteEvent(String id) {
        eventService.deleteEventById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
