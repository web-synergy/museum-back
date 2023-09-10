package baza.trainee.repository;

import baza.trainee.domain.dto.event.EventPublication;
import baza.trainee.domain.model.Event;
import com.redis.om.spring.repository.RedisDocumentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface EventRepository extends RedisDocumentRepository<Event, String> {
    Page<Event> findAll(Pageable pageable);


    Event getEventById(String id);


    Event save(EventPublication newEvent);


    Event update(String id, EventPublication updatedEvent);


    void deleteEventById(String id);
}
