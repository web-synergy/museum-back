package baza.trainee.service.impl;

import baza.trainee.exceptions.custom.EmailSendingException;
import baza.trainee.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import static baza.trainee.constants.MailConstants.FAIL_SEND_MSG;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    @Value("${mail.template.path.museum}")
    private String museumTemplatePath;

    @Value("${mail.template.path.user}")
    private String userTemplatePath;

    @Value("${mail.museum.email}")
    private String museumEmail;

    @Value("${mail.museum.label}")
    private String museumLabel;

    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(final String to, final String message, final String subject) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(message, true);
            helper.setTo(to);
            helper.setFrom(museumEmail, museumLabel);
            helper.setSubject(subject);
            mailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException ex) {
            throw new EmailSendingException(FAIL_SEND_MSG);
        }
    }

    @Override
    public String buildMsgForUser() {
        return readHtmlTemplateFromFile(userTemplatePath);
    }

    @Override
    public String buildMsgForMuseum(final String firstName, final String lastName,
                                    final String email, final String message) {
        String emailTemplate = readHtmlTemplateFromFile(museumTemplatePath);

        emailTemplate = emailTemplate.replace("{{firstName}}", firstName);
        emailTemplate = emailTemplate.replace("{{lastName}}", lastName);
        emailTemplate = emailTemplate.replace("{{email}}", email);
        emailTemplate = emailTemplate.replace("{{message}}", message);

        return emailTemplate;
    }

    private String readHtmlTemplateFromFile(final String filePath) {
        ClassPathResource resource = new ClassPathResource(filePath);

        byte[] byteArray;
        try {
            byteArray = FileCopyUtils.copyToByteArray(resource.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Impossible to read the file with the letter template.");
        }
        return new String(byteArray, StandardCharsets.UTF_8);
    }
}
