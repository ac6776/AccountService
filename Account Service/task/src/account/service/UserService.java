package account.service;

import account.domain.Role;
import account.domain.User;
import account.exceptions.AdminDeletionException;
import account.exceptions.PasswordAlreadyInUseException;
import account.exceptions.RolesAssignmentException;
import account.exceptions.UserExistException;
import account.repository.UserRepository;
import account.security.UserDetailsImpl;
import account.service.events.*;
import account.service.events.RoleRemovedEvent;
import account.service.events.DeleteUserEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private UserRepository repository;
    private PasswordEncoder passwordEncoder;
    private RoleService roleService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setRepository(UserRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(User user) {
        if (repository.findByEmailIgnoreCase(user.getEmail()).isPresent())
            throw new UserExistException();
        user.setEmail(user.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role adminRole = roleService.findRole("ROLE_ADMINISTRATOR");
        Role userRole = roleService.findRole("ROLE_USER");
        user.grandAuthority(repository.findAll().isEmpty() ? adminRole : userRole);
        User createdUser = repository.save(user);
        publisher.publishEvent(new UserCreatedEvent(user));
        return createdUser;
    }

    public User findByEmail(String email) throws UsernameNotFoundException {
        return repository.findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByEmailIgnoreCase(username).orElseThrow(() ->
                new UsernameNotFoundException("User with provided email [%s] not found".formatted(username)));
        return new UserDetailsImpl(user);
    }

    public User updatePassword(String email, String newPassword) {
        User user = findByEmail(email);
        if (passwordEncoder.matches(newPassword, user.getPassword()))
            throw new PasswordAlreadyInUseException();
        user.setPassword(passwordEncoder.encode(newPassword));
        User updatedUser = repository.save(user);
        publisher.publishEvent(new ChangePasswordEvent(user));
        return updatedUser;
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public void deleteUser(String email) {
        User user = findByEmail(email);
        for (Role role: user.getRoles()) {
            if (role.toString().equals("ROLE_ADMINISTRATOR")) {
                throw new AdminDeletionException();
            }
        }
        repository.delete(user);
        publisher.publishEvent(new DeleteUserEvent(user)); //
    }


    public User updateRoles(String email, String operation, String role) {
        User user = findByEmail(email);
        ApplicationEvent event;
        if (operation.equals("GRANT")) {
            grantRole(user, role);
            event = new RoleGrantedEvent(user);
        } else {
            removeRole(user, role);
            event = new RoleRemovedEvent(user);
        }
        User updatedUser = repository.save(user);
        publisher.publishEvent(event);
        return updatedUser;
    }

    private void removeRole(User user, String role) {
        Role roleFromDB = roleService.findRole(role);
        if (role.equals("ROLE_ADMINISTRATOR")) {
            throw new RolesAssignmentException("Can't remove ADMINISTRATOR role!");
        }
        if (!user.getRoles().contains(roleFromDB)) {
            throw new RolesAssignmentException("The user does not have a role!");
        }
        if (user.getRoles().size() == 1) {
            throw new RolesAssignmentException("The user must have at least one role!");
        }
        user.removeAuthority(roleFromDB);
    }

    private void grantRole(User user, String role) {
        Role roleFromDB = roleService.findRole(role);
        if ((user.isAdmin() && !role.equals("ROLE_ADMINISTRATOR")) ||
                (!user.isAdmin() && role.equals("ROLE_ADMINISTRATOR"))) {
            throw new RolesAssignmentException("The user cannot combine administrative and business roles!");
        }
        user.grandAuthority(roleFromDB);
    }

    public User updateLockUser(String email, String operation) {
        User user = findByEmail(email);
        ApplicationEvent event;
        if (operation.equals("LOCK")) {
            user.setLocked(true);
            event = new UserLockedEvent(user);
        } else {
            user.setLocked(false);
            event = new UserUnlockedEvent(user);
        }
        User updatedUser = repository.save(user);
        publisher.publishEvent(event);
        return updatedUser;
    }

    public void resetLoginAttempts(User user) {
        user.setLoginAttempts(0);
        repository.save(user);
    }
}
