package baza.trainee.service;

import baza.trainee.dto.LoginDto;

public interface AdminLoginService {
    /**
     * Save setting login for change login, build link
     * and send link to new user email.
     *
     * @param loginDto    Logins for change
     * @param userLogin Current user
     */
    void checkAndSaveSettingLogin(LoginDto loginDto, String userLogin);
    /**
     * Change old login to new login.
     *
     * @param cod    Activation cod for change login
     */
    void changeLogin(String cod);
}
