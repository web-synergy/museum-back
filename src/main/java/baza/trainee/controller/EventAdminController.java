package baza.trainee.controller;

import baza.trainee.domain.dto.event.EventPublication;
import baza.trainee.domain.model.Event;
import baza.trainee.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static baza.trainee.utils.ControllerUtils.handleFieldsErrors;

/**
 * Spring MVC REST controller serving event operations for admin users.
 *
 * @author Oleksandr Korkach
 */
@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class EventAdminController {

    private final EventService eventService;

    /**
     * @param request       The EventPublication containing information about the event to be created.
     * @param bindingResult for validation exception specifying.
     * @return Saved event.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new event", description = "Creates a new event with the provided information.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Event created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public Event createEvent(
            @Parameter(description = "Event data to be created")
            @RequestBody @Valid final EventPublication request,
            final BindingResult bindingResult
    ) {
        handleFieldsErrors(bindingResult);

        return eventService.save(request);
    }

    /**
     * Update an existing event identified by its unique identifier.
     *
     * @param id            The unique identifier of the event to be updated.
     * @param request       The EventPublication containing the updated information for the event.
     * @param bindingResult for validation exception specifying.
     * @return Updated event.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing event",
            description = "Updates an existing event with the provided information.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Event updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Event not found")
    })
    public Event updateEvent(
            @Parameter(description = "Unique identifier of the event to be updated")
            @PathVariable("id") final String id,
            @Parameter(description = "Event data to be updated")
            @RequestBody @Valid final EventPublication request,
            final BindingResult bindingResult
    ) {
        handleFieldsErrors(bindingResult);

        return eventService.update(id, request);
    }

    /**
     * Delete an existing event identified by its unique identifier.
     *
     * @param id The unique identifier of the event to be deleted.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete an existing event",
            description = "Deletes an existing event by its unique identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Event deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Event not found")
    })
    public void deleteEvent(
            @Parameter(description = "Unique identifier of the event to be deleted")
            @PathVariable("id") final String id) {
        eventService.deleteEventById(id);
    }
}
