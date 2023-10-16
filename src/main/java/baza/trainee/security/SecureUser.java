package baza.trainee.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import baza.trainee.domain.model.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SecureUser implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        var roles = this.user.getRoles().stream()
                .map(s -> "ROLE_" + s)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        var scope = this.user.getScope().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        var authorities = new ArrayList<GrantedAuthority>();
        authorities.addAll(roles);
        authorities.addAll(scope);

        return authorities;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
