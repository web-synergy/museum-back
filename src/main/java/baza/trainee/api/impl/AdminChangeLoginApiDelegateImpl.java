package baza.trainee.api.impl;

import baza.trainee.api.AdminChangeLoginApiDelegate;
import baza.trainee.dto.UpdateLoginRequest;
import baza.trainee.service.AdminLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminChangeLoginApiDelegateImpl implements AdminChangeLoginApiDelegate {

    private final AdminLoginService adminLoginService;

    @Override
    public ResponseEntity<Void> approveUpdateLogin(String code) {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        adminLoginService.approveUpdateLogin(code, username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> updateLogin(UpdateLoginRequest updateLoginRequest) {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        adminLoginService.checkAndSaveSettingLogin(updateLoginRequest, username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
