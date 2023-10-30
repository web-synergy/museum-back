package web.synergy.config;

import web.synergy.domain.model.Event;
import web.synergy.domain.model.MuseumData;
import web.synergy.repository.EventRepository;
import web.synergy.repository.MuseumDataRepository;
import com.redis.om.spring.annotations.EnableRedisDocumentRepositories;

import web.synergy.domain.model.Article;
import web.synergy.repository.ArticleRepository;

import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRedisDocumentRepositories(basePackageClasses = {
        EventRepository.class,
        ArticleRepository.class,
        MuseumData.class,
        Event.class,
        Article.class,
        MuseumDataRepository.class
})
public class RedisOmConfig {
}
