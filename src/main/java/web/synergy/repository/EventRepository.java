package web.synergy.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import web.synergy.domain.model.Event;
import com.redis.om.spring.repository.RedisDocumentRepository;

import java.util.Comparator;
import java.util.Optional;


public interface EventRepository extends RedisDocumentRepository<Event, String> {

    default Page<Event> findAllByStatusOrderByCreatedDesc(String status, Pageable pageable) {
        var count = count();
        long offset = pageable.getOffset();
        var content = findAll()
                .stream()
                .filter(e -> e.getStatus().equals(status))
                .sorted(Comparator.naturalOrder())
                .skip(offset)
                .limit(pageable.getPageSize())
                .toList();

        return new PageImpl<>(content, pageable, count);
    }

    Optional<Event> findBySlug(String slug);

    default Page<Event> findAllSorted(Pageable pageable) {
        var count = count();
        long offset = pageable.getOffset();
        var content = findAll()
                .stream()
                .sorted(Comparator.naturalOrder())
                .skip(offset)
                .limit(pageable.getPageSize())
                .toList();

        return new PageImpl<>(content, pageable, count);
    }
}
