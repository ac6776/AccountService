package account.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "employees")
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
    @NotEmpty(message = "password required")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}
