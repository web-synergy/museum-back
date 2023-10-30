package baza.trainee.api.impl;

import baza.trainee.api.AdminUpdateCredentialsApiDelegate;
import baza.trainee.dto.EmailUpdateRequest;
import baza.trainee.service.MailService;
import baza.trainee.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static baza.trainee.constants.MailConstants.ACTIVATION_CODE;
import static baza.trainee.domain.enums.Templates.PASSWORD_RECOVERY;
import static baza.trainee.domain.enums.Templates.UPDATE_LOGIN;

@Service
@RequiredArgsConstructor
public class AdminUpdateCredentialsApiDelegateImpl implements AdminUpdateCredentialsApiDelegate {

    private static final int PASSWORD_LENGTH = 8;

    private final UserService userService;
    private final MailService mailService;

    @Override
    public ResponseEntity<Void> updatePassword(String password) {
        var userName = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.updatePassword(password, userName);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> recoverPassword(String email) {
        var randomPassword = generatePassword(PASSWORD_LENGTH);
        userService.updatePassword(randomPassword, email);

        var messageContent = mailService.buildHTMLMessageContent(PASSWORD_RECOVERY, randomPassword);
        mailService.sendEmail(email, messageContent, "Password recovery");

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> updateEmail(EmailUpdateRequest emailUpdateRequest) {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var emailForUpdate = emailUpdateRequest.getEmail();
        var confirmationCode = userService.updateEmail(emailForUpdate, username);

        var confirmationCodeMessage = mailService.buildHTMLMessageContent(UPDATE_LOGIN, confirmationCode);
        mailService.sendEmail(emailForUpdate, confirmationCodeMessage, ACTIVATION_CODE);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> confirmUpdateEmail(String code) {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.confirmUpdateEmail(code, username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private static String generatePassword(int length) {
        var capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        var lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        var numbers = "1234567890";
        var combinedChars = capitalCaseLetters + lowerCaseLetters + numbers;
        final char[] password = new char[length];

        final var random = ThreadLocalRandom.current();
        IntStream.range(0, length).forEach(i ->
                password[i] = combinedChars.charAt(random.nextInt(combinedChars.length())));

        return String.valueOf(password);
    }
}
