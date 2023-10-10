package baza.trainee.service;

import baza.trainee.domain.dto.LoginDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.userdetails.UserDetails;

public interface AdminLoginService {
    /**
     * Check login.
     *
     * @param oldLogin    Login for check
     * @param userDetails Current user
     */
    void checkLogin(String oldLogin, UserDetails userDetails);

    /**
     * Save setting login for change login, build link
     * and send link to new user email.
     *
     * @param loginDto    Logins for change
     * @param userDetails Current user
     * @param session     {@link HttpSession}
     */
    void checkAndSaveSettingLogin(LoginDto loginDto, UserDetails userDetails,
                                  HttpSession session);
    /**
     * Change old login to new login.
     *
     * @param cod    Activation cod for change login
     * @param session     {@link HttpSession}
     */
    void changeLogin(String cod, HttpSession session);
}
