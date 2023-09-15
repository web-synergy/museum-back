package baza.trainee.controller;

import baza.trainee.domain.dto.MailDto;
import baza.trainee.service.MailService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static baza.trainee.constants.MailConstants.MUSEUM_SUBJECT;
import static baza.trainee.utils.ControllerUtils.handleFieldsErrors;

/**
 * Spring MVC REST controller for handling feedback submission.
 *
 * @author Anatolii Omelchenko
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(("/api/feedback"))
public class MailController {

    @Value("${mail.museum.email}")
    private String museumEmail;

    private final MailService mailService;

    /**
     * Handles the submission of a contact form. Sends emails to the user and the museum.
     *
     * @param mailDto       An object containing data from the feedback form,
     *                      including first name, last name, email, message.
     * @param bindingResult An object that holds information about data binding and validation errors, if any.
     * @return A ResponseEntity with a status of 200 (OK) if the email sending is successful.
     */
    @PostMapping("/submit")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Email sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<?> submitContactForm(
            @Parameter(description = "Feedback form data")
            @Valid @RequestBody final MailDto mailDto,
            final BindingResult bindingResult
    ) {
        handleFieldsErrors(bindingResult);

        String msgForUser = mailService.buildMsgForUser(mailDto.firstName(), mailDto.lastName());
        mailService.sendEmail(mailDto.email(), msgForUser, MUSEUM_SUBJECT);

        String msgForMuseum = mailService.buildMsgForMuseum(mailDto.firstName(), mailDto.lastName(),
                mailDto.email(), mailDto.message());
        mailService.sendEmail(museumEmail, msgForMuseum, MUSEUM_SUBJECT);

        return ResponseEntity.ok().build();
    }

}
