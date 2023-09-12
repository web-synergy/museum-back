package baza.trainee.service.impl;

import baza.trainee.domain.dto.event.EventPublication;
import baza.trainee.domain.mapper.EventMapper;
import baza.trainee.domain.model.Event;
import baza.trainee.exceptions.custom.EntityNotFoundException;
import baza.trainee.repository.EventRepository;
import baza.trainee.service.EventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public EventServiceImpl(EventRepository eventRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    @Override
    public Page<Event> getAll(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }



    @Override
    public Event getById(String id) {
        return eventRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Event", "ID: " + id));
    }

    @Override
    public Event save(EventPublication newEvent) {
        Event event = eventMapper.toEvent(newEvent);
        eventRepository.save(event);
        return event;
    }

    @Override
    public Event update(String id, EventPublication updatedEvent) {
        Event event = eventMapper.toEvent(updatedEvent);
        event.setId(id);
        eventRepository.save(event);
        return event;
    }

    @Override
    public void deleteEventById(String id) {
        eventRepository.deleteById(id);
    }
}
