package web.synergy.service;

import org.springframework.data.domain.Page;
import web.synergy.domain.model.Event;
import org.springframework.data.domain.Pageable;

public interface EventService {

    /**
     * Retrieve Events page by Pageable object.
     * 
     * @param pageable {@link Pageable} object.
     * @return A Page of events.
     */
    Page<Event> getAll(Pageable pageable);

    /**
     * Retrieve published Events page by Pageable object.
     *
     * @param pageable described the page.
     * @return Page of published Events.
     */
    Page<Event> getPublished(Pageable pageable);

    /**
     * Create a new event based on the provided EventPublicationDto.
     *
     * @param event    The EventPublication containing information about the event
     *                 to be created.
     * @param username - admin`s username
     * @return Saved event.
     */
    Event save(Event event, String username);

    /**
     * Update an existing event identified by id.
     *
     * @param event    The EventPublication containing updated information for
     *                 the event.
     * @param slug     The unique identifier of the event to be updated.
     * @param username - admin`s username
     * @return Updated event.
     */
    Event update(String slug, Event event, String username);

    /**
     * Delete an event identified by its id.
     *
     * @param slug The unique identifier of the event to be deleted.
     */
    void deleteEventBySlug(String slug);

    /**
     * Retrieve detailed information about a specific event by its slug.
     *
     * @param slug The unique Slug of the event.
     * @return An Event with given Slug.
     */
    Event getBySlug(String slug);
}
