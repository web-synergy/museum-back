package baza.trainee.controller;

import baza.trainee.domain.dto.MailDto;
import baza.trainee.exceptions.custom.EmailSendingException;
import baza.trainee.service.MailService;
import baza.trainee.utils.LoggingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static baza.trainee.constants.MailConstants.FAIL_SEND_MSG;
import static baza.trainee.constants.MailConstants.MUSEUM_EMAIL;
import static baza.trainee.constants.MailConstants.MUSEUM_SUBJECT;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MailController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MailService mailService;

    @MockBean
    private LoggingService loggingService;

    private MailDto validMailDto;
    private MailDto notValidMailDto;

    @BeforeAll
    public void setUp() {
        validMailDto = new MailDto("John", "Doe", "test@gmail.com", "User message");
        notValidMailDto = new MailDto("John", "Doe", "invalid_email", "User message");
    }

    @Test
    public void testSubmitContactFormWithValidData() throws Exception {
        when(mailService.buildMsgForUser(any(), any())).thenReturn("Message for user");
        when(mailService.buildMsgForMuseum(any(), any(), any(), any())).thenReturn("Message for museum");

        mockMvc.perform(post("/api/feedback/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validMailDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(mailService).sendEmail(validMailDto.email(), "Message for user", MUSEUM_SUBJECT);
        verify(mailService).sendEmail(MUSEUM_EMAIL, "Message for museum", MUSEUM_SUBJECT);
    }

    @Test
    public void testSubmitContactFormWithInvalidData() throws Exception {
        mockMvc.perform(post("/api/feedback/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(notValidMailDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("email - must be a well-formed email address;")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void SubmitContactFormWithSendingError() throws Exception {
        doThrow(new EmailSendingException(FAIL_SEND_MSG))
                .when(mailService).sendEmail(any(), any(),any());

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
