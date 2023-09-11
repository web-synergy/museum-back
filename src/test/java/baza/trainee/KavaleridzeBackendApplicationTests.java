package baza.trainee;

import baza.trainee.integration.RedisTestConfig;
import baza.trainee.service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import({ RedisTestConfig.class })
class KavaleridzeBackendApplicationTests {

    @MockBean
    private EventService eventService;

    @Test
    void contextLoads() {
    }

}
