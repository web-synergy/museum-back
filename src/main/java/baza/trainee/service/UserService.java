package baza.trainee.service;

public interface UserService {

    String createRootUser(String email, String rawPassword);

    void updatePassword(String password, String userName);

    String updateEmail(String emailForUpdate, String currentEmail);

    void confirmUpdateEmail(String code, String userLogin);

}
