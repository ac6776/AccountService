package account.controller;

import account.domain.User;
import account.messages.UserDeleteSuccessMessage;
import account.service.UserService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("${endpoint.get.user}")
    public List<User> getUsers() {
        return userService.findAll();
    }

    @DeleteMapping("${endpoint.delete.user}/{email}")
    public UserDeleteSuccessMessage deleteUser(@AuthenticationPrincipal UserDetails admin, @PathVariable String email, WebRequest request) {
        userService.deleteUser(admin.getUsername(), email, request.getDescription(false));
        return new UserDeleteSuccessMessage(email, "Deleted successfully!");
    }

    @PutMapping("${endpoint.put.role}")
    public User updateRoles(@AuthenticationPrincipal UserDetails admin, @RequestBody @Valid RoleUpdate object, WebRequest request) {
        return userService.updateRoles(admin.getUsername(), object.getUser(), object.getOperation(), object.getRole(), request.getDescription(false));
    }

    @PutMapping("${endpoint.put.access}")
    public Map<String, String> putLockStatus(@AuthenticationPrincipal UserDetails admin, @RequestBody @Valid LockOperation object, WebRequest request) {
        userService.updateLockUser(admin.getUsername(), object.getUser(), object.getOperation(), request.getDescription(false));
        return Map.of("status", "User %s %sed!".formatted(object.getUser(), object.getOperation()));
    }

    @NoArgsConstructor
    @Getter
    @Setter
    static class LockOperation {
        @NotNull
        private String user;
        @Pattern(regexp = "LOCK|UNLOCK")
        private String operation;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    static class RoleUpdate {
        @NotNull
        private String user;
        @NotNull
        private String role;
        @NotNull
        @Pattern(regexp = "GRANT|REMOVE")
        private String operation;

        public void setRole(String role) {
            String prefix = "ROLE_";
            this.role = role.startsWith(prefix) ? role : prefix + role;
        }

        @Override
        public String toString() {
            return "User: %s, role: %s, operation: %s".formatted(user, role, operation);
        }
    }
}
