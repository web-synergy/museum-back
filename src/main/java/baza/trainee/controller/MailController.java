package baza.trainee.controller;

import baza.trainee.domain.dto.MailDto;
import baza.trainee.service.MailService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static baza.trainee.constants.MailConstants.MUSEUM_EMAIL;
import static baza.trainee.constants.MailConstants.MUSEUM_SUBJECT;
import static baza.trainee.utils.ControllerUtils.handleFieldsErrors;

@AllArgsConstructor
@RestController("/api/feedback")
public class MailController {

    private final MailService mailService;

    /**
     * Handles the submission of a contact form. Sends emails to the user and the museum.
     *
     * @param mailDto An object containing data from the feedback form, including first name, last name, email, message.
     * @param bindingResult An object that holds information about data binding and validation errors, if any.
     * @return A ResponseEntity with a status of 200 (OK) if the email sending is successful.
     * @throws IOException If there are any input/output errors during the email processing.
     */
    @PostMapping("/submit")
    public ResponseEntity<?> submitContactForm(@Valid @RequestBody final MailDto mailDto,
                                               final BindingResult bindingResult) throws IOException {
        handleFieldsErrors(bindingResult);

        String msgForUser = mailService.buildMsgForUser(mailDto.firstName(), mailDto.lastName());
        mailService.sendEmail(mailDto.email(), msgForUser, MUSEUM_SUBJECT);

        String msgForMuseum = mailService.buildMsgForMuseum(mailDto.firstName(), mailDto.lastName(),
                mailDto.email(), mailDto.message());
        mailService.sendEmail(MUSEUM_EMAIL, msgForMuseum, MUSEUM_SUBJECT);

        return ResponseEntity.ok().build();
    }

}
