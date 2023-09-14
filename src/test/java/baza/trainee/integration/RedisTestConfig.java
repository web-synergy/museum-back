package baza.trainee.integration;

import baza.trainee.domain.model.Article;
import baza.trainee.domain.model.Event;
import com.redis.om.spring.annotations.EnableRedisDocumentRepositories;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Jedis test configurations.
 *
 * @author Evhen Malysh
 */
@TestConfiguration
@EnableRedisDocumentRepositories(
        basePackageClasses = {
                TestEventRepository.class,
                TestArticleRepository.class,
                Event.class,
                Article.class
        })
public class RedisTestConfig {

//    @Bean
//    JedisConnectionFactory jedisConnectionFactory() {
//        return new JedisConnectionFactory();
//    }
//
//    @Bean
//    RedisTemplate<String, Object> redisTemplate() {
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(jedisConnectionFactory());
//        return template;
//    }

}
