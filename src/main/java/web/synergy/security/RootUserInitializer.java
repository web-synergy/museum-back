package web.synergy.security;

import web.synergy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RootUserInitializer implements CommandLineRunner {

    private final UserService userService;

    @Value("${users.root.credentials.username}")
    private String rootUsername;

    @Value("${users.root.credentials.password}")
    private String rootPassword;

    @Override
    public void run(String... args) {
        userService.createRootUser(rootUsername, rootPassword);
    }

}
