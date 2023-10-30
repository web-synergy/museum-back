package web.synergy.service.impl;

import web.synergy.domain.enums.Templates;
import web.synergy.exceptions.custom.EmailSendingException;
import web.synergy.service.MailService;
import web.synergy.utils.FileSystemStorageUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

import static web.synergy.constants.MailConstants.FAIL_SEND_MSG;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    @Value("${mail.template.path.museum}")
    private String museumFeedbackTemplatePath;

    @Value("${mail.template.path.user}")
    private String userFeedbackTemplatePath;

    @Value("${mail.template.path.change_login}")
    private String updateLoginTemplatePath;

    @Value("${mail.template.path.password_recovery}")
    private String passwordRecoveryTemplatePath;

    @Value("${mail.museum.email}")
    private String museumEmail;

    @Value("${mail.museum.label}")
    private String museumLabel;

    private final JavaMailSender mailSender;

    @Override
    public String buildHTMLMessageContent(Templates template, String... parameters) {
        return switch (template) {
            case MUSEUM_FEEDBACK ->
                    buildMuseumMessageContent(parameters[0], parameters[1], parameters[2], parameters[3]);
            case USER_FEEDBACK -> buildUserMessageContent();
            case UPDATE_LOGIN -> buildUpdateLoginMessageContent(parameters[0]);
            case PASSWORD_RECOVERY -> buildPasswordRecoveryMessageContent(parameters[0]);
        };
    }

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

    private String buildUserMessageContent() {
        return readHtmlTemplateFromFile(userFeedbackTemplatePath);
    }

    private String buildMuseumMessageContent(
            final String firstName,
            final String lastName,
            final String email,
            final String message
    ) {
        return readHtmlTemplateFromFile(museumFeedbackTemplatePath)
                .replace("{{firstName}}", firstName)
                .replace("{{lastName}}", lastName)
                .replace("{{email}}", email)
                .replace("{{message}}", message);
    }

    private String buildUpdateLoginMessageContent(final String code) {
        return readHtmlTemplateFromFile(updateLoginTemplatePath)
                .replace("{{code}}", code);
    }

    private String buildPasswordRecoveryMessageContent(String password) {
        return readHtmlTemplateFromFile(passwordRecoveryTemplatePath)
                .replace("{{password}}", password);
    }

    private String readHtmlTemplateFromFile(final String filePath) {
        ClassPathResource resource = new ClassPathResource(filePath);
        byte[] byteArray = FileSystemStorageUtils.getByteArrayFromResource(resource);

        return new String(byteArray);
    }
}
