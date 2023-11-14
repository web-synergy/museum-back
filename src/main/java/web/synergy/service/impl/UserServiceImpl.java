package web.synergy.service.impl;

import web.synergy.domain.enums.OpsKey;
import web.synergy.exceptions.custom.LoginNotValidException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import web.synergy.domain.enums.Role;
import web.synergy.domain.enums.Scope;
import web.synergy.domain.model.User;
import web.synergy.repository.UserRepository;
import web.synergy.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static web.synergy.domain.enums.OpsKey.CONFIRM_CODE_KEY;
import static web.synergy.domain.enums.OpsKey.EMAIL_KEY;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    public static final String NOT_VALID_CONFIRMATION_CODE = "Not valid confirmation code.";
    public static final String NOT_FIND_USER_BY_LOGIN = "Not find user by login";
    public static final String USER_ALREADY_EXISTS = "This user already exists in the database";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StringRedisTemplate template;

    @Value("${mail.museum.key_expiration_time}")
    private long keyExpirationTime;

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

    @Override
    public boolean isEmpty() {
        return userRepository.findAll().isEmpty();
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
