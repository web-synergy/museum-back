package baza.trainee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    RedisTemplate<String, String> stringRedisTemplate(RedisConnectionFactory connectionFactory){
        RedisTemplate<String, String> template = new RedisTemplate<>();
        StringRedisSerializer serializer = new StringRedisSerializer();
        template.setKeySerializer(serializer);
        template.setValueSerializer(serializer);
        template.setConnectionFactory(connectionFactory);
        return template;
    }
}
