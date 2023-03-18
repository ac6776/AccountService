package account.controller;

import account.domain.User;
import account.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
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
import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<?> changePass(@AuthenticationPrincipal UserDetails userDetails, @RequestBody JsonNode payload, Errors errors, HttpServletRequest request) {
//        Map<String, String> responseBody = new HashMap<>();
//        responseBody.put("email", userDetails.getUsername());
//        responseBody.put("status", "The password has been updated successfully");
        String newPassword = payload.get("new_password").textValue();
        return service.updatePassword(userDetails.getUsername(), newPassword);
    }
}