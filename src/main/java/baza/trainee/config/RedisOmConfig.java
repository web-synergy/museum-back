package baza.trainee.config;

import baza.trainee.domain.model.Article;
import baza.trainee.service.impl.ArticleServiceImpl;
import com.redis.om.spring.annotations.EnableRedisDocumentRepositories;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRedisDocumentRepositories(basePackageClasses = {Article.class, ArticleServiceImpl.class})
public class RedisOmConfig {
}
