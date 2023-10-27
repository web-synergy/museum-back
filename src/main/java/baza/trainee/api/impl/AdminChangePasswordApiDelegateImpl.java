package baza.trainee.api.impl;

import baza.trainee.api.AdminChangePasswordApiDelegate;
import baza.trainee.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminChangePasswordApiDelegateImpl implements AdminChangePasswordApiDelegate {

    private final UserService userService;

    @Override
    public ResponseEntity<Void> updatePassword(String password) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.updatePassword(password, userName);
        return ResponseEntity.noContent().build();
    }
}
