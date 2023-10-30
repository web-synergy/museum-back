package web.synergy.repository;

import web.synergy.domain.model.Event;
import com.redis.om.spring.repository.RedisDocumentRepository;


public interface EventRepository extends RedisDocumentRepository<Event, String> { }
