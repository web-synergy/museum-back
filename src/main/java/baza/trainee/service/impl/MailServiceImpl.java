package baza.trainee.service.impl;

import baza.trainee.exceptions.custom.EmailSendingException;
import baza.trainee.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import static baza.trainee.constants.MailConstants.FAIL_SEND_MSG;
import static baza.trainee.constants.MailConstants.MUSEUM_EMAIL;
import static baza.trainee.constants.MailConstants.MUSEUM_LABEL;
import static baza.trainee.constants.MailConstants.MUSEUM_TEMPLATE_PATH;
import static baza.trainee.constants.MailConstants.USER_TEMPLATE_PATH;

@Service
@AllArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(final String to, final String message, final String subject) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(message, true);
            helper.setTo(to);
            helper.setFrom(MUSEUM_EMAIL, MUSEUM_LABEL);
            helper.setSubject(subject);
            mailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException ex) {
            throw new EmailSendingException(FAIL_SEND_MSG);
        }
    }

    @Override
    public String buildMsgForUser(final String firstName, final String lastName) throws IOException {
        String emailTemplate = readHtmlTemplateFromFile(USER_TEMPLATE_PATH);

        emailTemplate = emailTemplate.replace("{{firstName}}", firstName);
        emailTemplate = emailTemplate.replace("{{lastName}}", lastName);

        return emailTemplate;
    }

    @Override
    public String buildMsgForMuseum(final String firstName, final String lastName,
                                    final String email, final String message) throws IOException {
        String emailTemplate = readHtmlTemplateFromFile(MUSEUM_TEMPLATE_PATH);

        emailTemplate = emailTemplate.replace("{{firstName}}", firstName);
        emailTemplate = emailTemplate.replace("{{lastName}}", lastName);
        emailTemplate = emailTemplate.replace("{{email}}", email);
        emailTemplate = emailTemplate.replace("{{message}}", message);

        return emailTemplate;
    }

    private String readHtmlTemplateFromFile(final String filePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(filePath);

        byte[] byteArray = FileCopyUtils.copyToByteArray(resource.getInputStream());
        return new String(byteArray, StandardCharsets.UTF_8);
    }
}
