package account.domain;

import account.security.RoleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private RoleType role;
    public Role(String role) {
        this.role = RoleType.valueOf(role);
    }

    @Override
    public String toString() {
        return role.name();
    }
}
