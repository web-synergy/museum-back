package web.synergy.repository;

import web.synergy.domain.model.Event;
import com.redis.om.spring.repository.RedisDocumentRepository;

import java.util.Optional;


public interface EventRepository extends RedisDocumentRepository<Event, String> {
    Optional<Event> findByTitle(String title);

    boolean existsByTitle(String eventTitle);
}
