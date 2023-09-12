package baza.trainee.integration;

import baza.trainee.domain.model.Article;
import com.redis.om.spring.repository.RedisDocumentRepository;

/**
 * Repository implements RedisDocumentRepository that provides operations with
 * {@link Article} objects for integration tests.
 *
 * @author Evhen Malysh
 */
interface TestArticleRepository extends RedisDocumentRepository<Article, String> {
}
