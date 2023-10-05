package baza.trainee.api.impl;

import baza.trainee.api.FeedbackApiDelegate;
import baza.trainee.dto.MailDto;
import baza.trainee.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static baza.trainee.constants.MailConstants.MUSEUM_SUBJECT;

@Service
@RequiredArgsConstructor
public class FeedbackApiDelegateImpl implements FeedbackApiDelegate {

    @Value("${mail.museum.email}")
    private String museumEmail;

    private final MailService mailService;

    @Override
    public ResponseEntity<Object> submitContactForm(MailDto mailDto) {
        String msgForUser = mailService.buildMsgForUser();
        mailService.sendEmail(mailDto.getEmail(), msgForUser, MUSEUM_SUBJECT);

        String msgForMuseum = mailService.buildMsgForMuseum(mailDto.getFirstName(), mailDto.getLastName(),
                mailDto.getEmail(), mailDto.getMessage());
        mailService.sendEmail(museumEmail, msgForMuseum, MUSEUM_SUBJECT);

        return ResponseEntity.ok().build();
    }

}
