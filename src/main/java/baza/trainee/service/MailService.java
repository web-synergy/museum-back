package baza.trainee.service;

public interface MailService {

    /**
     * Sends an email from the specified email address.
     *
     * @param to      The email address of the recipient.
     * @param message The message text to be sent in the email.
     * @param subject The subject of the email.
     */
    void sendEmail(String to, String message, String subject);
}
