package baza.trainee.constants;

public final class MailConstants {
    private MailConstants() {
    }

    public static final String MUSEUM_EMAIL = "museum@gmail.com";
    public static final String MUSEUM_LABEL = "Museum.ua";
    public static final String MUSEUM_SUBJECT = "Лист зворотного зв'язку";
    public static final String FAIL_SEND_MSG = "failed to send email";
    public static final String MUSEUM_TEMPLATE_PATH = "templates/msg_for_museum.html";
    public static final String USER_TEMPLATE_PATH = "templates/msg_for_user.html";
    public static final int MIN_NAME_LENGTH = 2;
    public static final int MAX_NAME_LENGTH = 30;
    public static final int MIN_MSG_LENGTH = 10;
    public static final int MAX_MSG_LENGTH = 300;
}
