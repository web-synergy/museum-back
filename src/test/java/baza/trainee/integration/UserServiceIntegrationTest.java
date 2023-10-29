package baza.trainee.integration;

import baza.trainee.domain.model.User;
import baza.trainee.repository.UserRepository;
import baza.trainee.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceIntegrationTest extends AbstractIntegrationTest{

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        var user = new User();
        user.setEmail("root@email.com");
        user.setPassword(passwordEncoder.encode("password"));

        userRepository.save(user);
    }

    @Test
    void afterCallingIncrementLoginCounter_loginCounterShouldBeIncrementedByOne() {
        // when:
        int counter1 = userService.incrementLoginCounter("root@email.com");

        // then:
        assertEquals(1, counter1);

        // when:
        int counter2 = userService.incrementLoginCounter("root@email.com");

        // then:
        assertEquals(2, counter2);
    }
}