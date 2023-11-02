package web.synergy.api.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import web.synergy.api.AdminEventsApiDelegate;
import web.synergy.domain.mapper.EventMapper;
import web.synergy.domain.mapper.PageEventMapper;
import web.synergy.dto.EventDraft;
import web.synergy.dto.EventPublication;
import web.synergy.dto.EventResponse;
import web.synergy.dto.PageEvent;
import web.synergy.service.EventService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminEventsDelegateImpl implements AdminEventsApiDelegate {

    private final EventService eventService;
    private final EventMapper eventMapper;
    private final PageEventMapper pageEventMapper;

    @Override
    public ResponseEntity<EventResponse> createDraft(EventDraft eventDraft) {
        var username = getUsername();
        var event = eventMapper.toEvent(eventDraft);
        var savedEvent = eventService.save(event, username);
        var eventResponse = eventMapper.toResponse(savedEvent);

        return new ResponseEntity<>(eventResponse, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<EventResponse> updateDraft(String slug, EventDraft eventDraft) {
        var username = getUsername();
        var eventForUpdate = eventMapper.toEvent(eventDraft);
        var savedEvent = eventService.update(slug, eventForUpdate, username);
        var eventResponse = eventMapper.toResponse(savedEvent);

        return new ResponseEntity<>(eventResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<EventResponse> createEvent(EventPublication eventPublication) {
        var username = getUsername();
        var event = eventMapper.toEvent(eventPublication);        
        var savedEvent = eventService.save(event, username);
        var eventResponse = eventMapper.toResponse(savedEvent);

        return new ResponseEntity<>(eventResponse, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<EventResponse> updateEvent(String slug, EventPublication eventPublication) {
        var username = getUsername();
        var event = eventMapper.toEvent(eventPublication);
        var updatedEvent = eventService.update(slug, event, username);
        var eventResponse = eventMapper.toResponse(updatedEvent);

        return new ResponseEntity<>(eventResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteEvent(String slug) {
        eventService.deleteEventBySlug(slug);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<PageEvent> getAll(Integer size, Integer page) {
        var pageable = PageRequest.of(page, size);
        var pageEventResponses = eventService.getAll(pageable)
                .map(eventMapper::toResponse);
        var pageEvent = pageEventMapper.toPageEvent(pageEventResponses);
        return new ResponseEntity<>(pageEvent, HttpStatus.OK);
    }

    private static String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
