package account.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@Getter
@Setter
public class RoleUpdateDTO {
    @NotNull
    private String user;
    @NotNull
    private String role;
    @NotNull
    @Pattern(regexp = "GRANT|REMOVE")
    private String operation;

    public void setRole(String role) {
        String prefix = "ROLE_";
        this.role = role.startsWith(prefix) ? role : prefix + role;
    }

    @Override
    public String toString() {
        return "User: %s, role: %s, operation: %s".formatted(user, role, operation) ;
    }
}
