package baza.trainee.controller;

import baza.trainee.domain.model.Event;
import baza.trainee.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring MVC REST controller serving event operations.
 *
 * @author Oleksandr Korkach
 */
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    /**
     * Get a page of events.
     *
     * @param size page size.
     * @param page number of the page.
     * @return A page of events.
     */
    @GetMapping
    @Operation(summary = "Get a page of events",
            description = "Retrieves a page of events with the specified size and page number.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page of events retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public Page<Event> getAll(
            @Parameter(description = "Size of the page")
            @RequestParam("size") final int size,
            @Parameter(description = "Page number")
            @RequestParam("page") final int page
    ) {
        var pageable = Pageable.ofSize(size).withPage(page);
        return eventService.getAll(pageable);
    }

    /**
     * Get an event by its ID.
     *
     * @param id The event id for looking a certain event
     * @return An event.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get an event by ID", description = "Retrieves an event by its unique identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Event retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Event not found")
    })
    public Event getById(
            @Parameter(description = "Unique identifier of the event")
            @PathVariable final String id
    ) {
        return eventService.getById(id);
    }
}
