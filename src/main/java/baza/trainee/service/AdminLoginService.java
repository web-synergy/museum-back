package baza.trainee.service;

import baza.trainee.dto.UpdateLoginRequest;

public interface AdminLoginService {

    /**
     * Save setting login for change login, build link
     * and send link to new user email.
     *
     * @param updateLoginRequest    Logins for change
     * @param userLogin Current user login
     */
    void checkAndSaveSettingLogin(UpdateLoginRequest updateLoginRequest, String userLogin);

    /**
     * Change old login to new login.
     *
     * @param code     Activation code for change login
     * @param userLogin Current userLogin.
     */
    void approveUpdateLogin(String code, String userLogin);
}
