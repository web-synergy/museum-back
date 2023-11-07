package web.synergy.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import web.synergy.domain.model.Event;
import com.redis.om.spring.repository.RedisDocumentRepository;

import java.util.Optional;


public interface EventRepository extends RedisDocumentRepository<Event, String> {

    Page<Event> findAllByStatusOrderByCreatedDesc(String status, Pageable pageable);

    Optional<Event> findBySlug(String slug);
}
