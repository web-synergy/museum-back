package web.synergy.service.impl;

import web.synergy.domain.mapper.EventMapper;
import web.synergy.domain.mapper.PageEventMapper;
import web.synergy.domain.model.Event;
import web.synergy.dto.EventPublication;
import web.synergy.dto.EventResponse;
import web.synergy.dto.PageEvent;
import web.synergy.repository.EventRepository;
import web.synergy.service.EventService;
import web.synergy.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.synergy.utils.ExceptionUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final ImageService imageService;
    private final EventMapper eventMapper;
    private final PageEventMapper pageMapper;

    @Override
    public PageEvent getAll(Pageable pageable) {
        return pageMapper.toPageEvent(eventRepository.findAll(pageable)
                .map(eventMapper::toResponse));
    }

    @Override
    public EventResponse getById(String id) {
        var event = eventRepository.findById(id)
                .orElseThrow(ExceptionUtils.getNotFoundExceptionSupplier(Event.class, "ID: " + id));
        return eventMapper.toResponse(event);
    }

    @Override
    @Transactional
    public EventResponse save(EventPublication event, String username) {
        var eventToSave = eventMapper.toEvent(event);

        Optional.ofNullable(event.getBanner())
                .filter(imageId -> !imageId.isBlank() || !imageId.isEmpty())
                .ifPresent(imageId -> imageService.persist(imageId, username));

        var savedEvent = eventRepository.save(eventToSave);

        return eventMapper.toResponse(savedEvent);
    }

    @Override
    @Transactional
    public EventResponse update(String id, EventPublication publication, String username) {
        var eventToUpdate = eventRepository.findById(id)
                .orElseThrow(ExceptionUtils.getNotFoundExceptionSupplier(Event.class, "ID: " + id));

        var eventForUpdate = eventMapper.toEvent(publication);
        eventForUpdate.setId(eventToUpdate.getId());
        eventForUpdate.setCreated(eventToUpdate.getCreated());

        if (eventToUpdate.getBanner() != null) {
            var existingBanner = eventToUpdate.getBanner();

            if (eventForUpdate.getBanner() != null
                    && !eventForUpdate.getBanner().equals(existingBanner)) {
                var bannerToUpdate = eventForUpdate.getBanner();

                imageService.deleteImage(existingBanner);
                imageService.persist(bannerToUpdate, username);
            } else if (eventForUpdate.getBanner() == null) {
                imageService.deleteImage(existingBanner);
            }
        } else if (eventForUpdate.getBanner() != null) {
            var bannerToUpdate = eventForUpdate.getBanner();

            imageService.persist(bannerToUpdate, username);
        }

        var updatedEvent = eventRepository.update(eventForUpdate);

        return eventMapper.toResponse(updatedEvent);
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
}
