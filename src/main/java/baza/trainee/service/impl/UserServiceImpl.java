package baza.trainee.service.impl;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String createRootUser(String email, String rawPassword) {
        var encodedPassword = passwordEncoder.encode(rawPassword);

        var rootUser = new User();
        rootUser.setEmail(email);
        rootUser.setPassword(encodedPassword);
        rootUser.addRole(Role.ROOT);
        rootUser.addRole(Role.ADMIN);
        rootUser.addScope(Scope.READ);
        rootUser.addScope(Scope.WRITE);

        return userRepository.save(rootUser).getId();
    }

    @Override
    public void updatePassword(String password, String email) {
        var user = getUserByEmail(email);
        var encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        userRepository.update(user);
    }

    @Override
    public Integer incrementLoginCounter(String email) {
        var user = getUserByEmail(email);
        int counter = user.incrementLoginCounter();
        userRepository.update(user);

        return counter;
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(String.format("User with email %s was not found!", email)));
    }
}
