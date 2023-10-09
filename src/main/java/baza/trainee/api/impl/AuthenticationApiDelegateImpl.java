package baza.trainee.api.impl;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import baza.trainee.api.AuthenticationApiDelegate;
import baza.trainee.dto.SuccessAuthResponse;

@Service
public class AuthenticationApiDelegateImpl implements AuthenticationApiDelegate {

    @Override
    public ResponseEntity<SuccessAuthResponse> login() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        var authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        var response = new SuccessAuthResponse();
        response.roles(authorities);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> logout() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
