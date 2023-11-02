package web.synergy.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.synergy.domain.model.Event;
import web.synergy.exceptions.custom.EntityAlreadyExistsException;
import web.synergy.repository.EventRepository;
import web.synergy.service.EventService;
import web.synergy.service.ImageService;
import web.synergy.utils.ExceptionUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final ImageService imageService;

    @Override
    public Page<Event> getAll(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    @Override
    public Event getById(String id) {
        return eventRepository.findById(id)
                .orElseThrow(ExceptionUtils.getNotFoundExceptionSupplier(Event.class, "ID: " + id));
    }

    @Override
    @Transactional
    public Event save(Event event, String username) {
        Optional.ofNullable(event.getBanner())
                .filter(imageId -> !imageId.isBlank() || !imageId.isEmpty())
                .ifPresent(imageId -> imageService.persist(imageId, username));

        return eventRepository.save(event);
    }

    @Override
    @Transactional
    public Event update(String id, Event event, String username) {
        var eventToUpdate = eventRepository.findById(id)
                .orElseThrow(ExceptionUtils.getNotFoundExceptionSupplier(Event.class, "ID: " + id));

        event.setId(eventToUpdate.getId());
        event.setCreated(eventToUpdate.getCreated());

        if (eventToUpdate.getBanner() != null) {
            var existingBanner = eventToUpdate.getBanner();

            if (event.getBanner() != null
                    && !event.getBanner().equals(existingBanner)) {
                var bannerToUpdate = event.getBanner();

                imageService.deleteImage(existingBanner);
                imageService.persist(bannerToUpdate, username);
            } else if (event.getBanner() == null) {
                imageService.deleteImage(existingBanner);
            }
        } else if (event.getBanner() != null) {
            var bannerToUpdate = event.getBanner();

            imageService.persist(bannerToUpdate, username);
        }

        return eventRepository.update(event);
    }

    @Override
    @Transactional
    public void deleteEventById(String id) {
        var existingEvent = getById(id);

        Optional.ofNullable(existingEvent.getBanner())
                .filter(imageId -> !imageId.isBlank() || !imageId.isEmpty())
                .ifPresent(imageService::deleteImage);

        eventRepository.deleteById(id);
    }

    @Override
    public void isExists(String eventTitle) {
        if (eventRepository.existsByTitle(eventTitle)) {
            throw new EntityAlreadyExistsException("Event", "Title: " + eventTitle);
        }
    }

    @Override
    public Event getBySlug(String slug) {
        return eventRepository.findBySlug(slug)
                .orElseThrow(ExceptionUtils.getNotFoundExceptionSupplier(Event.class, "Slug: " + slug));
    }
}
