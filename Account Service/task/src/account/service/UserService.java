package account.service;

import account.domain.Role;
import account.domain.User;
import account.exceptions.AdminDeletionException;
import account.exceptions.PasswordAlreadyInUseException;
import account.exceptions.UserExistException;
import account.repository.UserRepository;
import account.security.RoleType;
import account.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return repository.save(user);
    }

    public User findByEmail(String email) throws UsernameNotFoundException {
        return repository.findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User with provided email [%s] not found".formatted(email)));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByEmail(username);
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
        return repository.findAll(Sort.by(Sort.Direction.ASC));
    }

    public void deleteUser(String email) {
        User user = findByEmail(email);
        for (Role role: user.getRoles()) {
            if (role.getRole() == RoleType.ROLE_ADMINISTRATOR) {
                throw new AdminDeletionException();
            }
        }
        repository.delete(user);
    }

    public User updateRoles(String email, String operation, String role) {
        User user = findByEmail(email);
        Role roleFromDB = roleService.findRole(role);
        if (operation.equals("GRANT")) {
            user.grantAuthority(roleFromDB);
        }
        if (operation.equals("REMOVE")) {
            user.removeAuthority(roleFromDB);
        }
        return repository.save(user);
    }
}
