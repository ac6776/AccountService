package account.service;

import account.domain.User;
import account.exceptions.PasswordAlreadyInUseException;
import account.exceptions.UserExistException;
import account.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }

    public User updatePassword(String email, String newPassword) {
        User user = findByEmail(email);
        if (passwordEncoder.matches(newPassword, user.getPassword()))
            throw new PasswordAlreadyInUseException();
        user.setPassword(passwordEncoder.encode(newPassword));
        return repository.save(user);
    }
}
