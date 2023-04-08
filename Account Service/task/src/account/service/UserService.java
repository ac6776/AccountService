package account.service;

import account.domain.Role;
import account.domain.SecurityEvent;
import account.domain.User;
import account.exceptions.AdminDeletionException;
import account.exceptions.PasswordAlreadyInUseException;
import account.exceptions.RolesAssignmentException;
import account.exceptions.UserExistException;
import account.repository.UserRepository;
import account.security.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private static final int MAX_LOGIN_ATTEMPTS = 5;

    private UserRepository repository;
    private PasswordEncoder passwordEncoder;
    private RoleService roleService;

    private Logger logger = LoggerFactory.getLogger(UserService.class);

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

    public User createUser(User user, String path) {
        if (repository.findByEmailIgnoreCase(user.getEmail()).isPresent())
            throw new UserExistException();
        user.setEmail(user.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role adminRole = roleService.findRole("ROLE_ADMINISTRATOR");
        Role userRole = roleService.findRole("ROLE_USER");
        user.grandAuthority(repository.findAll().isEmpty() ? adminRole : userRole);
        User createdUser = repository.save(user);
        SecurityEvent event = new SecurityEvent(EventType.CREATE_USER,"Anonymous", createdUser.getEmail(), path);
        publisher.publishEvent(new ApplicationSecurityEvent(createdUser, event));

        logger.info(event.toString());
        logger.info(createdUser.toString());

        return createdUser;
    }

    public User findByEmail(String email) throws UsernameNotFoundException {
        return repository.findByEmailIgnoreCase(email)
                .orElse(null);
//                .orElseThrow(() ->
//                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByEmailIgnoreCase(username).orElseThrow(() ->
                new UsernameNotFoundException("User with provided email [%s] not found".formatted(username)));
        return new UserDetailsImpl(user);
    }

    public User updatePassword(String email, String newPassword, String path) {
        User user = findByEmail(email);
        if (passwordEncoder.matches(newPassword, user.getPassword()))
            throw new PasswordAlreadyInUseException();
        user.setPassword(passwordEncoder.encode(newPassword));
        User updatedUser = repository.save(user);
        SecurityEvent event = new SecurityEvent(EventType.CHANGE_PASSWORD, user.getEmail(), user.getEmail(), path);
        publisher.publishEvent(new ApplicationSecurityEvent(user, event));
        return updatedUser;
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public void deleteUser(String adminEmail, String userEmail, String path) {
        User user = findByEmail(userEmail);
        for (Role role: user.getRoles()) {
            if (role.toString().equals("ROLE_ADMINISTRATOR")) {
                throw new AdminDeletionException();
            }
        }
        SecurityEvent event = new SecurityEvent(EventType.DELETE_USER, adminEmail, user.getEmail(), path);
        repository.delete(user);
        publisher.publishEvent(new ApplicationSecurityEvent(user, event)); //
    }


    public User updateRoles(String adminEmail, String userEmail, String operation, String role, String path) {
        User user = findByEmail(userEmail);
        SecurityEvent event;
        if (operation.equals("GRANT")) {
            grantRole(user, role);
            event = new SecurityEvent(EventType.GRANT_ROLE, adminEmail, user.getEmail(), path);
        } else {
            removeRole(user, role);
            event = new SecurityEvent(EventType.REMOVE_ROLE, adminEmail, user.getEmail(), path);
        }
        User updatedUser = repository.save(user);
        event.setAdditionalData(role);
        publisher.publishEvent(new ApplicationSecurityEvent(user, event));

        logger.info(event.toString());
        logger.info(updatedUser.toString());

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

    public User updateLockUser(String adminEmail, String userEmail, String operation, String path) {
        User user = findByEmail(userEmail);
        if (user == null) {
            return null;
        }
        SecurityEvent event;
        if (operation.equals("LOCK")) {
            user.setLocked(true);
            event = new SecurityEvent(EventType.LOCK_USER, adminEmail, "Lock user " + user.getEmail(), path);
        } else {
            user.setLocked(false);
            user.setLoginAttempts(0);
            event = new SecurityEvent(EventType.UNLOCK_USER, adminEmail, "Unlock user " + user.getEmail(), path);
        }
        User updatedUser = repository.save(user);

        publisher.publishEvent(new ApplicationSecurityEvent(user, event));
        return updatedUser;
    }

    public void resetLoginAttempts(User user) {
        user.setLoginAttempts(0);
        repository.save(user);
    }

    public User incrementLoginAttempts(String email, String path) {
        User user = findByEmail(email);
        if (user == null) {
            return null;
        }
//        User userUpdated = repository.save(user.incrementLoginAttempts());
        user.incrementLoginAttempts();
        if (user.getLoginAttempts() >= MAX_LOGIN_ATTEMPTS) {
            user.setLocked(true);
            publisher.publishEvent(new ApplicationSecurityEvent(user,
                    new SecurityEvent(EventType.BRUTE_FORCE, user.getEmail(), path, path)));
            publisher.publishEvent(new ApplicationSecurityEvent(user,
                    new SecurityEvent(EventType.LOCK_USER, user.getEmail(), "Lock user " + user.getEmail(), path)));
        }
        return repository.save(user);
    }
}
