package account.service;

import account.config.PasswordAlreadyInUseException;
import account.config.UserExistException;
import account.domain.User;
import account.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService implements UserDetailsService {
    private UserRepository repository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setRepository(UserRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User save(User user) {
        user.setEmail(user.getEmail());
        if (repository.findByEmailIgnoreCase(user.getEmail()).isPresent())
            throw new UserExistException();
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

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }

    public ResponseEntity<?> updatePassword(String email, String newPassword) {
        User user = findByEmail(email);
        if (passwordEncoder.matches(newPassword, user.getPassword()))
            throw new PasswordAlreadyInUseException();
        user.setPassword(passwordEncoder.encode(newPassword));
        repository.save(user);
        var body = Map.of("email", user.getEmail(), "status", "The password has been updated successfully");
        return ResponseEntity.ok(body);
    }
}
