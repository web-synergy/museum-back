package baza.trainee.repository;

import baza.trainee.domain.model.Event;
import com.redis.om.spring.repository.RedisDocumentRepository;


public interface EventRepository extends RedisDocumentRepository<Event, String> { }
