package account.security;

import account.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserDetailsImpl implements UserDetails {
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> rolesAndAuthorities;

    public UserDetailsImpl(User user) {
        this.username = user.getEmail();
        this.password = user.getPassword();
        this.rolesAndAuthorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.toString())).toList();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return rolesAndAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
