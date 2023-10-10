package baza.trainee.controller;

import baza.trainee.domain.dto.LoginDto;
import baza.trainee.service.AdminLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Spring MVC REST controller serving login operations for admin users.
 *
 * @author Andry Sitarskiy
 * @version 1.0
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminLoginController {
    private final AdminLoginService adminLoginService;

    /**
     * Check old login matches in form.
     *
     * @param oldLogin    Entered login from form
     * @param userDetails Current user
     * @return Login matches
     */
    @GetMapping("/checkOldLogin")
    @Operation(summary = "Check entered login to login of current user",
            description = "Login of current user return from user details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Entered login valid"),
            @ApiResponse(responseCode = "400", description = "Invalid entered login")
    })
     ResponseEntity<String> checkOldLogin(@Email final String oldLogin,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        adminLoginService.checkLogin(oldLogin, userDetails);
        return ResponseEntity.ok("Login is valid");
    }

    /**
     * Save setting login and send link user email.
     *
     * @param loginDto    Logins for change
     * @param userDetails Current user
     * @param session     {@link HttpSession}
     * @return Save setting for change login
     */
    @PostMapping("/saveSettingLogin")
    @Operation(summary = "Save setting for change login",
            description = "Save old login, new login and generate change code in session."
                    + "Send code in message to email of new login.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Save setting for change login"),
            @ApiResponse(responseCode = "400", description = "Invalid entered login")
    })
    ResponseEntity<String> saveSettingLogin(final LoginDto loginDto,
                                            @AuthenticationPrincipal UserDetails userDetails,
                                            HttpSession session) {
        adminLoginService.checkAndSaveSettingLogin(loginDto, userDetails, session);
        return ResponseEntity.ok("Save setting for change login");
    }
    /**
     * Change old login to new login.
     *
     * @param code    Code change login
     * @param session     {@link HttpSession}
     * @return Change login
     */
    @PutMapping("/changeLogin")
    @Operation(summary = "Change old login to new login of current user.",
            description = "Check entered code to code of session."
                    + "Find admin by login and change old login to new login in session.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Change login"),
            @ApiResponse(responseCode = "400", description = "Not change login")
    })
    ResponseEntity<String> changeLogin(final String code, HttpSession session) {
        adminLoginService.changeLogin(code, session);
        return ResponseEntity.ok("Change login");
    }
}
