package web.synergy.api.impl;

import java.util.stream.Collectors;

import web.synergy.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import web.synergy.api.AuthenticationApiDelegate;
import web.synergy.dto.SuccessAuthResponse;
import web.synergy.security.TokenService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationApiDelegateImpl implements AuthenticationApiDelegate {

    private final TokenService tokenService;
    private final UserService userService;

    @Override
    public ResponseEntity<SuccessAuthResponse> login() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        var authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        var response = new SuccessAuthResponse();
        response.roles(authorities);

        var generatedToken = tokenService.generateToken(authentication);
        response.setToken(generatedToken);

        int counter = userService.incrementLoginCounter(authentication.getName());
        response.setLoginCounter(counter);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> logout() {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
