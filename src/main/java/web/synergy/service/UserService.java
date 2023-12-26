package web.synergy.service;

import java.util.List;

import web.synergy.domain.model.User;

public interface UserService {

    User createRootUser(String email, String rawPassword);

    void updatePassword(String password, String userName);

    void updateEmail(String currentEmail, String emailForUpdate);

    Integer incrementLoginCounter(String email);

    String requestToUpdateEmail(String emailForUpdate, String currentEmail);

    void confirmUpdateEmail(String code, String userLogin);

    boolean isEmpty();

    List<User> getAll();

    void delete(String email);
}
