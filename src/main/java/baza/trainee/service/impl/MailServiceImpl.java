package baza.trainee.service.impl;

import baza.trainee.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@AllArgsConstructor
public class MailServiceImpl implements MailService {
    public static final String FAIL_SEND_MSG = "failed to send email";
    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(final String to, final String message, final String subject) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(message, true);
            helper.setTo(to);
            helper.setFrom("<noreply@museum.ua>", "Museum.ua");
            helper.setSubject(subject);
            mailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException ex) {
            throw new IllegalArgumentException(FAIL_SEND_MSG);
        }
    }
}
