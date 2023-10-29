package baza.trainee.api.impl;

import static baza.trainee.constants.MailConstants.MUSEUM_SUBJECT;
import static baza.trainee.domain.enums.Templates.MUSEUM_FEEDBACK;
import static baza.trainee.domain.enums.Templates.USER_FEEDBACK;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import baza.trainee.api.FeedbackApiDelegate;
import baza.trainee.dto.MailDto;
import baza.trainee.service.MailService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedbackApiDelegateImpl implements FeedbackApiDelegate {

    @Value("${mail.museum.email}")
    private String museumEmail;

    private final MailService mailService;

    @Override
    public ResponseEntity<Void> submitContactForm(MailDto mailDto) {
        String msgForUser = mailService.buildHTMLMessageContent(USER_FEEDBACK);
        mailService.sendEmail(mailDto.getEmail(), msgForUser, MUSEUM_SUBJECT);

        String msgForMuseum = mailService.buildHTMLMessageContent(
                MUSEUM_FEEDBACK,
                mailDto.getFirstName(),
                mailDto.getLastName(),
                mailDto.getEmail(),
                mailDto.getMessage()
        );
        mailService.sendEmail(museumEmail, msgForMuseum, MUSEUM_SUBJECT);

        return ResponseEntity.noContent().build();
    }

}
