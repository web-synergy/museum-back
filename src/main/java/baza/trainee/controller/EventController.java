package baza.trainee.controller;

import baza.trainee.domain.model.Event;
import baza.trainee.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    /**
     * @param size page size.
     * @param page number of the page.
     * @return  A page of events.
     */
    @GetMapping
    public Page<Event> getAll(
            @RequestParam("size")final int size,
            @RequestParam("page") final int page
    ) {
        var pageable = Pageable.ofSize(size).withPage(page);
        return eventService.getAll(pageable);
    }

    /**
     * @param id The event id for looking a certain event
     * @return An event.
     */
    @GetMapping("/{id}")
    public Event getById(final @PathVariable String id) {
        return eventService.getById(id);
    }
}
