package baza.trainee.service;

import baza.trainee.dto.LoginDto;
import jakarta.servlet.http.HttpSession;

public interface AdminLoginService {
    /**
     * Check login.
     *
     * @param enteredLogin    Login from form
     * @param userLogin Current user login
     */
    void checkLogin(String enteredLogin, String userLogin);

    /**
     * Save setting login for change login, build link
     * and send link to new user email.
     *
     * @param loginDto    Logins for change
     * @param userLogin Current user
     * @param session     {@link HttpSession}
     */
    void checkAndSaveSettingLogin(LoginDto loginDto, String userLogin,
                                  HttpSession session);
    /**
     * Change old login to new login.
     *
     * @param cod    Activation cod for change login
     * @param session     {@link HttpSession}
     */
    void changeLogin(String cod, HttpSession session);
}
