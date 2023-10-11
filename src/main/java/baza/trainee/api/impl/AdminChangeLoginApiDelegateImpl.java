package baza.trainee.api.impl;

import baza.trainee.api.AdminChangeLoginApiDelegate;
import baza.trainee.dto.LoginDto;
import baza.trainee.service.AdminLoginService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminChangeLoginApiDelegateImpl implements AdminChangeLoginApiDelegate {
    private final AdminLoginService adminLoginService;
    private final HttpServletRequest httpServletRequest;
    @Override
    public ResponseEntity<Void> changeLogin(String code) {
        adminLoginService.changeLogin(code, httpServletRequest.getSession());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> checkLogin(String oldLogin) {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        adminLoginService.checkLogin(oldLogin, username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> saveLogin(LoginDto loginDto) {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        adminLoginService.checkAndSaveSettingLogin(loginDto, username,
                httpServletRequest.getSession());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
