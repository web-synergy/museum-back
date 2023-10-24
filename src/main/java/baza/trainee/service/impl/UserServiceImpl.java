package baza.trainee.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import baza.trainee.domain.enums.Role;
import baza.trainee.domain.enums.Scope;
import baza.trainee.domain.model.User;
import baza.trainee.repository.UserRepository;
import baza.trainee.service.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public String createRootUser(String email, String rawPassword) {
        String encodedPassword = passwordEncoder.encode(rawPassword);

        User rootUser = new User();
        rootUser.setEmail(email);
        rootUser.setPassword(encodedPassword);
        rootUser.addRole(Role.ROOT);
        rootUser.addRole(Role.ADMIN);
        rootUser.addScope(Scope.READ);
        rootUser.addScope(Scope.WRITE);

        return adminRepository.save(rootUser).getId();
    }
}
