package web.synergy.integration;

import web.synergy.security.RootUserInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

/**
 * Abstract Test class that start Redis-stack container for integration tests.
 *
 * @author Evhen Malysh
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Testcontainers
abstract class AbstractIntegrationTest {

    private static final String REDIS_STACK_IMAGE = "redis/redis-stack";
    private static final int REDIS_PORT = 6379;

    private static GenericContainer<?> redis;

    static {
        redis = new GenericContainer<>(DockerImageName.parse(REDIS_STACK_IMAGE))
                .withExposedPorts(REDIS_PORT);
        redis.start();
        System.setProperty("spring.data.redis.host", redis.getHost());
        System.setProperty("spring.data.redis.port", redis.getMappedPort(REDIS_PORT).toString());
    }

    @MockBean
    private RootUserInitializer rootUserInitializer;

}
