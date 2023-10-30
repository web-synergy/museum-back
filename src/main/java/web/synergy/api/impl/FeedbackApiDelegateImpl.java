package web.synergy.api.impl;

import static web.synergy.constants.MailConstants.MUSEUM_SUBJECT;
import static web.synergy.domain.enums.Templates.MUSEUM_FEEDBACK;
import static web.synergy.domain.enums.Templates.USER_FEEDBACK;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import web.synergy.api.FeedbackApiDelegate;
import web.synergy.dto.MailDto;
import web.synergy.service.MailService;
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
