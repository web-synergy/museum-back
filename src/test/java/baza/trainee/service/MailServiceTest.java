package baza.trainee.service;

import baza.trainee.integration.RedisTestConfig;
import baza.trainee.service.impl.MailServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import({ RedisTestConfig.class })
public class MailServiceTest {

    @Autowired
    private MailServiceImpl mailService;

    @Test
    void testBuildMsgForUser() throws IOException {
        String result = mailService.buildMsgForUser("John", "Doe");
        assertThat(result).contains("John", "Doe");
    }

    @Test
    void testBuildMsgForMuseum() throws IOException {
        String result = mailService.buildMsgForMuseum("John", "Doe",
                "test@example.com", "Message");
        assertThat(result).contains("John", "Doe", "test@example.com", "Message");
    }
}
