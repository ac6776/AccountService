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
}
