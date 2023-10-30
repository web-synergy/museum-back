package web.synergy.repository;

import web.synergy.domain.model.Article;
import com.redis.om.spring.repository.RedisDocumentRepository;

public interface ArticleRepository extends RedisDocumentRepository<Article, String> {

}
