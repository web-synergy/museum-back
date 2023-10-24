package baza.trainee.service;

import baza.trainee.dto.EventPublication;
import baza.trainee.dto.EventResponse;
import baza.trainee.dto.PageEvent;
import org.springframework.data.domain.Pageable;

public interface EventService {

    /**
     * @param pageable {@link Pageable} object.
     * @return A Page of events.
     */
    PageEvent getAll(Pageable pageable);

    /**
     * Retrieve detailed information about a specific event by its id.
     * 
     * @param id The unique identifier of the event.
     * @return An Event with given ID.
     */
    EventResponse getById(String id);

    /**
     * Create a new event based on the provided EventPublicationDto.
     *
     * @param event    The EventPublication containing information about the event
     *                 to be created.
     * @param username - admin`s username
     * @return Saved event.
     */
    EventResponse save(EventPublication event, String username);

    /**
     * Update an existing event identified by id.
     * 
     * @param event    The EventPublication containing updated information for
     *                 the event.
     * @param id       The unique identifier of the event to be updated.
     * @param username - admin`s username
     * @return Updated event.
     */
    EventResponse update(String id, EventPublication event, String username);

    /**
     * Delete an event identified by its id.
     * 
     * @param id The unique identifier of the event to be deleted.
     */
    void deleteEventById(String id);
}
