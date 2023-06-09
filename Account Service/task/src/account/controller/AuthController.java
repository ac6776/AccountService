package account.controller;

import account.domain.User;
import account.messages.CustomErrorMessage;
import account.messages.PasswordUpdateSuccessfulMessage;
import account.security.passwordvalidator.Password;
import account.service.UserService;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private UserService service;

    @Autowired
    public void setService(UserService service) {
        this.service = service;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody User user, Errors errors, HttpServletRequest request) {
        if (errors.hasFieldErrors()) {
            return ResponseEntity.badRequest()
                    .body(new CustomErrorMessage(LocalDateTime.now().toString(),
                            HttpStatus.BAD_REQUEST.value(),
                            HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            errors.getFieldError().getDefaultMessage(),
                            request.getServletPath()));
        }
        return ResponseEntity.ok(service.save(user));
    }

    @PostMapping("/changepass")
    public ResponseEntity<?> changePass(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody NewPasswordObject newPasswordObject, Errors errors, HttpServletRequest request) {
        if (errors.hasFieldErrors()) {
            return ResponseEntity.badRequest()
                    .body(new CustomErrorMessage(LocalDateTime.now().toString(),
                            HttpStatus.BAD_REQUEST.value(),
                            HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            errors.getFieldError().getDefaultMessage(),
                            request.getServletPath()));
        }
        User user = service.updatePassword(userDetails.getUsername(), newPasswordObject.getNewPassword());
        return ResponseEntity.ok(new PasswordUpdateSuccessfulMessage(user.getEmail(), "The password has been updated successfully"));
    }

    @NoArgsConstructor
    @Getter
    @Setter
    static class NewPasswordObject {
        @Password
        @JsonProperty(value = "new_password", access = JsonProperty.Access.WRITE_ONLY)
        private String newPassword;
    }
}