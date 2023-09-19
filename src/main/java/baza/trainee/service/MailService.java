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

    /**
     * Builds a message for a user.
     *
     * @return A formatted html-message string for user.
     */
    String buildMsgForUser();

    /**
     * Builds a message for a museum, including user information and a custom message.
     *
     * @param firstName The first name of the user.
     * @param lastName  The last name of the user.
     * @param email     The email address of the user.
     * @param message   The message from user.
     * @return A formatted html-message string for the museum that includes user information and message.
     */
    String buildMsgForMuseum(String firstName, String lastName, String email, String message);
}
