package baza.trainee.service;

import baza.trainee.domain.enums.Templates;

public interface MailService {

    /**
     * Sends an email from the specified email address.
     *
     * @param to      The email address of the recipient.
     * @param message The message text to be sent in the email.
     * @param subject The subject of the email.
     */
    void sendEmail(String to, String message, String subject);

    /**
     * Build HTML message content from template with a given parameters.
     *
     * @param template   message template.
     * @param parameters template params.
     * @return formatted HTML content.
     */
    String buildHTMLMessageContent(Templates template, String... parameters);

}
