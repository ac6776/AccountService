package account.service;

import account.domain.Role;
import account.domain.User;
import account.exceptions.AdminDeletionException;
import account.exceptions.PasswordAlreadyInUseException;
import account.exceptions.RolesAssignmentException;
import account.exceptions.UserExistException;
import account.repository.UserRepository;
import account.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
public class UserService implements UserDetailsService {
    private UserRepository repository;
    private PasswordEncoder passwordEncoder;

    private RoleService roleService;

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

    public User save(User user) {
        if (repository.findByEmailIgnoreCase(user.getEmail()).isPresent())
            throw new UserExistException();
        user.setEmail(user.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Map<String, Role> allRoles = roleService.findAll();
        Role role = allRoles.get(repository.findAll().isEmpty() ? "ADMINISTRATOR" : "USER");
        user.setRoles(List.of(role));
        return repository.save(user);
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
        return repository.save(user);
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
    }

    public User updateRoles(String email, String operation, String role) {
        User user = findByEmail(email);
        Role roleFromDB = roleService.findRole(role);
        if (operation.equals("GRANT")) {
            if ((user.isAdmin() && !role.equals("ROLE_ADMINISTRATOR")) ||
                (!user.isAdmin() && role.equals("ROLE_ADMINISTRATOR"))) {
                throw new RolesAssignmentException("The user cannot combine administrative and business roles!");
            }
            user.grandAuthority(roleFromDB);
        }
        if (operation.equals("REMOVE")) {
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
        return user;
    }
}
