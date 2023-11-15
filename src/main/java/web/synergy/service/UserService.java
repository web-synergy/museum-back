package web.synergy.service;

public interface UserService {

    String createRootUser(String email, String rawPassword);

    void updatePassword(String password, String userName);

    void updateEmail(String currentEmail, String emailForUpdate);

    Integer incrementLoginCounter(String email);

    String requestToUpdateEmail(String emailForUpdate, String currentEmail);

    void confirmUpdateEmail(String code, String userLogin);

    boolean isEmpty();
}
