package account.controller;

import account.domain.RoleUpdateDTO;
import account.domain.User;
import account.messages.UserDeleteSuccessMessage;
import account.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);

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
    public UserDeleteSuccessMessage deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
        return new UserDeleteSuccessMessage(email, "Deleted successfully!");
    }

    @PutMapping("${endpoint.put.role}")
    public User updateRoles(@RequestBody @Valid RoleUpdateDTO object) {
        return userService.updateRoles(object.getUser(), object.getOperation(), object.getRole());
    }

    @PutMapping("${endpoint.put.access}")
    public Map<String, String> putLockStatus() {
        return Map.of();
    }
}
