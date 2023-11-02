package web.synergy.api.impl;

import web.synergy.api.EventsApiDelegate;
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

    @Override
    public ResponseEntity<PageEvent> getAll(Integer size, Integer page) {
        var pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(eventService.getAll(pageable), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<EventResponse> getBySlug(String slug) {
        return new ResponseEntity<>(eventService.getBySlug(slug), HttpStatus.OK);
    }
}
