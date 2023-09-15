package baza.trainee.config;

import com.redis.om.spring.annotations.EnableRedisDocumentRepositories;

import baza.trainee.domain.model.Article;
import baza.trainee.domain.model.Event;
import baza.trainee.domain.model.ContentBlock;
import baza.trainee.repository.ArticleRepository;
import baza.trainee.repository.EventRepository;

import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRedisDocumentRepositories(basePackageClasses = {
        EventRepository.class,
        ArticleRepository.class,
        Event.class,
        Article.class,
        ContentBlock.class
})
public class RedisOmConfig {
}
