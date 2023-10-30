package web.synergy.service;

import web.synergy.security.RootUserInitializer;
import web.synergy.service.impl.MailServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import web.synergy.domain.enums.Templates;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MailServiceTest {

    @Autowired
    private MailServiceImpl mailService;

    @MockBean
    RootUserInitializer rootUserInitializer;

    @MockBean
    ArticleService articleService;

    @Test
    void testBuildMsgForUser() {
        String result = mailService.buildHTMLMessageContent(Templates.USER_FEEDBACK);
        assertThat(result).contains("Ваш запит відправлено. Очікуйте відповідь");
    }

    @Test
    void testBuildMsgForMuseum() {
        String result = mailService.buildHTMLMessageContent(Templates.MUSEUM_FEEDBACK, "John", "Doe",
                "test@example.com", "Message");
        assertThat(result).contains("John", "Doe", "test@example.com", "Message");
    }
}
