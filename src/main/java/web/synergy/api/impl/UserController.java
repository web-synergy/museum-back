package web.synergy.api.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.synergy.domain.model.User;
import web.synergy.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @DeleteMapping("/{email}")
    public void delete(@PathVariable("email") String email) {
        userService.delete(email);
    }

    @PostMapping
    public User create(@RequestBody UserRegistration registration) {
        return userService.createRootUser(registration.email(), registration.password());
    }

    public record UserRegistration(String email, String password) { }

}
