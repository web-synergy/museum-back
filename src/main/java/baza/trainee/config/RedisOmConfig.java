package baza.trainee.config;

import baza.trainee.domain.model.Event;
import baza.trainee.repository.EventRepository;
import com.redis.om.spring.annotations.EnableRedisDocumentRepositories;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRedisDocumentRepositories(basePackageClasses = { EventRepository.class, Event.class })
public class RedisOmConfig {
}
