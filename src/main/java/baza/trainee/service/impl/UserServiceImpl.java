package baza.trainee.service.impl;

import baza.trainee.domain.enums.OpsKey;
import baza.trainee.exceptions.custom.LoginNotValidException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import baza.trainee.domain.enums.Role;
import baza.trainee.domain.enums.Scope;
import baza.trainee.domain.model.User;
import baza.trainee.repository.UserRepository;
import baza.trainee.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static baza.trainee.domain.enums.OpsKey.CONFIRM_CODE_KEY;
import static baza.trainee.domain.enums.OpsKey.EMAIL_KEY;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String USER_NOT_FOUND_MESSAGE = "User with email: %s was not found!";
    public static final String NOT_VALID_CONFIRMATION_CODE = "Not valid confirmation code.";
    public static final String NOT_FIND_USER_BY_LOGIN = "Not find user by login";
    public static final String USER_ALREADY_EXISTS = "This user already exists in the database";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StringRedisTemplate template;

    @Value("${mail.museum.key_expiration_time}")
    private long keyExpirationTime;

    public String createRootUser(String email, String rawPassword) {
        String encodedPassword = passwordEncoder.encode(rawPassword);

        User rootUser = new User();
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
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, email)));
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        userRepository.update(user);
    }

    @Override
    public String updateEmail(String emailForUpdate, String currentEmail) {
        isEmailNotTaken(emailForUpdate);

        var confirmationCode = createConfirmationCode();
        cacheStringValue(EMAIL_KEY, currentEmail, emailForUpdate);
        cacheStringValue(CONFIRM_CODE_KEY, currentEmail, confirmationCode);

        return confirmationCode;
    }

    @Override
    public void confirmUpdateEmail(String actualCode, String authEmail) {
        var opsForValue = template.opsForValue();
        var expectedCode = opsForValue.get(CONFIRM_CODE_KEY + "_" + authEmail);
        var emailForUpdate = opsForValue.get(EMAIL_KEY + "_" + authEmail);

        if (!actualCode.equals(expectedCode)) throw new LoginNotValidException(NOT_VALID_CONFIRMATION_CODE);

        var admin = getUserByEmail(authEmail);
        admin.setEmail(emailForUpdate);
        userRepository.update(admin);

        opsForValue.getAndDelete(EMAIL_KEY + "_" + authEmail);
        opsForValue.getAndDelete(CONFIRM_CODE_KEY + "_" + authEmail);
    }

    private User getUserByEmail(String userLogin) {
        return userRepository.findByEmail(userLogin)
                .orElseThrow(() -> new UsernameNotFoundException(NOT_FIND_USER_BY_LOGIN));
    }

    private void isEmailNotTaken(final String username) {
        userRepository.findByEmail(username).ifPresent(user -> {
            throw new LoginNotValidException(USER_ALREADY_EXISTS);
        });
    }

    private void cacheStringValue(OpsKey keyPrefix, String keyPostfix, String value) {
        var key = keyPrefix + "_" + keyPostfix;
        template.opsForValue().set(key, value, keyExpirationTime, TimeUnit.MINUTES);
    }

    private String createConfirmationCode() {
        int randomNumber = ThreadLocalRandom.current().nextInt(999999);
        return String.format("%06d", randomNumber);
    }
}
