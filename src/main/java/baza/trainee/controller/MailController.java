package baza.trainee.controller;

import baza.trainee.domain.dto.MailDto;
import baza.trainee.service.MailService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@AllArgsConstructor
@RestController("/api/feedback")
public class MailController {

    private static final String MUSEUM_EMAIL = "museum@gmail.com";
    private static final String SUBJECT = "Лист зворотного зв'язку";
    private final MailService mailService;

    /**
     * Handles the submission of a contact form. Sends emails to the user and the museum.
     *
     * @param mailDto An object containing data from the feedback form, including first name, last name, email, message.
     * @return A ResponseEntity with a status of 200 (OK) if the email sending is successful.
     * @throws IOException If there are any input/output errors during the email processing.
     */
    @PostMapping("/submit")
    public ResponseEntity<?> submitContactForm(@RequestBody final MailDto mailDto) throws IOException {

        String msgForUser = mailService.buildMsgForUser(mailDto.firstName(), mailDto.lastName());
        mailService.sendEmail(mailDto.email(), msgForUser, SUBJECT);

        String msgForMuseum = mailService.buildMsgForMuseum(mailDto.firstName(), mailDto.lastName(),
                mailDto.email(), mailDto.message());
        mailService.sendEmail(MUSEUM_EMAIL, msgForMuseum, SUBJECT);

        return ResponseEntity.ok().build();
    }

}
