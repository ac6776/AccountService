package account.domain;

import account.domain.passwordvalidator.Password;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "employees")
@ToString
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @NotEmpty(message = "first name required")
    private String name;

    @NotNull
    @NotEmpty(message = "lastname required")
    private String lastname;

    @NotNull
    @Pattern(regexp = ".+@acme\\.com", message = "email should be @acme.com")
    private String email;

    @Password
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
    @JsonGetter("roles")
    public String[] getRolesAsArrayOfString() {
        return roles.stream().map(Role::toString).sorted().toArray(String[]::new);
    }

    @Column(name = "is_locked")
    @JsonIgnore
    private boolean isLocked = false;

    @Column(name = "login_attempts")
    @JsonIgnore
    private int loginAttempts = 0;

    @JsonIgnore
    public boolean isAdmin() {
        return roles.stream().map(Role::toString).anyMatch(s -> s.equals("ROLE_ADMINISTRATOR"));
    }

    public void grandAuthority(Role role) {
        if (roles == null)
            roles = new HashSet<>();
        roles.add(role);
    }

    public void removeAuthority(Role role) {
        roles.remove(role);
    }

    public User incrementLoginAttempts() {
        ++loginAttempts;
        return this;
    }

}
