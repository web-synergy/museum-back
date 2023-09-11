package baza.trainee.integration;

import org.junit.jupiter.api.AfterAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

/**
 * Abstract Test class that start Redis-stack container for integration tests.
 *
 * @author Evhen Malysh
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
@Import({ RedisTestConfig.class })
class AbstractIntegrationTest {

    private static final String REDIS_STACK_IMAGE = "redis/redis-stack:7.2.0-v0";
    private static final int REDIS_PORT = 6379;

    private static GenericContainer<?> redis;

    static {
        redis = new GenericContainer<>(DockerImageName.parse(REDIS_STACK_IMAGE))
                .withExposedPorts(REDIS_PORT);
        redis.start();
        System.setProperty("spring.data.redis.host", redis.getHost());
        System.setProperty("spring.data.redis.port", redis.getMappedPort(REDIS_PORT).toString());
    }

    @AfterAll
    static void tearDown() {
        redis.stop();
    }
}
