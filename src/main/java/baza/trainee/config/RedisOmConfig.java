package baza.trainee.config;

import baza.trainee.domain.model.Event;
import baza.trainee.repository.EventRepository;
import com.redis.om.spring.annotations.EnableRedisDocumentRepositories;

import baza.trainee.domain.model.Article;
import baza.trainee.domain.model.Event;
import baza.trainee.domain.model.ContentBlock;
import baza.trainee.repository.ArticleRepository;
import baza.trainee.repository.EventRepository;

import org.springframework.context.annotation.Configuration;

@Configuration
<<<<<<< HEAD
@EnableRedisDocumentRepositories(basePackageClasses = { EventRepository.class, Event.class })
=======
@EnableRedisDocumentRepositories(basePackageClasses = {
        EventRepository.class,
        ArticleRepository.class,
        Event.class,
        Article.class,
        ContentBlock.class
})
>>>>>>> 7041faccf1f57ef5be98448514cd4ec6d5aa9a38
public class RedisOmConfig {
}
