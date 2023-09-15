package baza.trainee;

import baza.trainee.service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class KavaleridzeBackendApplicationTests {

    @MockBean
    private EventService eventService;

    @Test
    void contextLoads() {
    }

}
