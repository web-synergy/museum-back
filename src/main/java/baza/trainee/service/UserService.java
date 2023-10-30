package baza.trainee.service;

public interface UserService {

    String createRootUser(String email, String rawPassword);

    void updatePassword(String password, String userName);

    Integer incrementLoginCounter(String email);
}
