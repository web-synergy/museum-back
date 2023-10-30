package web.synergy.controller;

import web.synergy.dto.MailDto;
import web.synergy.exceptions.custom.EmailSendingException;
import web.synergy.security.RootUserInitializer;
import web.synergy.service.ArticleService;
import web.synergy.service.MailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import web.synergy.domain.enums.Templates;

import static web.synergy.constants.MailConstants.FAIL_SEND_MSG;
import static web.synergy.constants.MailConstants.MUSEUM_SUBJECT;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FeedbackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MailService mailService;

    @MockBean
    private RootUserInitializer rootUserInitializer;

    @MockBean
    private ArticleService articleService;

    private MailDto validMailDto;
    private MailDto notValidMailDto;

    @Value("${mail.museum.email}")
    private String museumEmail;

    @BeforeAll
    public void setUp() {
        validMailDto = new MailDto("John", "Doe", "test@gmail.com", "User message");
        notValidMailDto = new MailDto("John", "Doe", "invalid_email", "User message");
    }

    @Test
    void testSubmitContactFormWithValidData() throws Exception {
        when(mailService.buildHTMLMessageContent(Templates.USER_FEEDBACK)).thenReturn("Message for user");
        when(mailService.buildHTMLMessageContent(eq(Templates.MUSEUM_FEEDBACK), anyString(), anyString(), anyString(), anyString()))
                .thenReturn("Message for museum");

        mockMvc.perform(post("/api/feedback/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validMailDto)))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(mailService).sendEmail(validMailDto.getEmail(), "Message for user", MUSEUM_SUBJECT);
        verify(mailService).sendEmail(museumEmail, "Message for museum", MUSEUM_SUBJECT);
    }

    @Test
    void testSubmitContactFormWithInvalidData() throws Exception {
        mockMvc.perform(post("/api/feedback/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(notValidMailDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", containsString(notValidMailDto.getEmail())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void SubmitContactFormWithSendingError() throws Exception {
        Mockito.doThrow(new EmailSendingException(FAIL_SEND_MSG))
                .when(mailService).sendEmail(any(), any(), any());

        mockMvc.perform(post("/api/feedback/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validMailDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(FAIL_SEND_MSG)))
                .andExpect(status().isInternalServerError());
    }
}
