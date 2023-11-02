package web.synergy.api.impl;

import web.synergy.api.EventsApiDelegate;
import web.synergy.domain.mapper.EventMapper;
import web.synergy.domain.mapper.PageEventMapper;
import web.synergy.dto.EventResponse;
import web.synergy.dto.PageEvent;
import web.synergy.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventApiDelegateImpl implements EventsApiDelegate {

    private final EventService eventService;
    private final PageEventMapper pageEventMapper;
    private final EventMapper eventMapper;

    @Override
    public ResponseEntity<PageEvent> getPublished(Integer size, Integer page) {
        var pageable = PageRequest.of(page, size);
        var eventResponsePage = eventService.getPublished(pageable).map(eventMapper::toResponse);
        var eventPage = pageEventMapper.toPageEvent(eventResponsePage);
        return new ResponseEntity<>(eventPage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<EventResponse> getBySlug(String slug) {
        var event = eventService.getBySlug(slug);
        var eventResponse = eventMapper.toResponse(event);
        return new ResponseEntity<>(eventResponse, HttpStatus.OK);
    }
}
