package baza.trainee.repository;

import baza.trainee.domain.model.Article;
import java.util.Optional;
import com.redis.om.spring.repository.RedisDocumentRepository;

public interface ArticleRepository extends RedisDocumentRepository<Article, String> {

    Optional<Article> findByTitle(String title);
}
