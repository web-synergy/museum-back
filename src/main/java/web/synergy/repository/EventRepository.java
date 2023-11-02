package web.synergy.repository;

import web.synergy.domain.model.Event;
import com.redis.om.spring.repository.RedisDocumentRepository;

import java.util.Optional;


public interface EventRepository extends RedisDocumentRepository<Event, String> {

    boolean existsByTitle(String eventTitle);

    Optional<Event> findBySlug(String slug);
}
