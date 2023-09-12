package baza.trainee.integration;

import baza.trainee.domain.model.Event;
import com.redis.om.spring.repository.RedisDocumentRepository;

/**
 * Repository implements RedisDocumentRepository that provides operations with
 * {@link Event} objects for integration tests.
 *
 * @author Evhen Malysh
 */
interface TestEventRepository extends RedisDocumentRepository<Event, String> {
}
