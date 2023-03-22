package account.domain;

import account.domain.uservalidator.UserExist;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "payments", uniqueConstraints = {
        @UniqueConstraint(name = "UniqueEmployeeAndPeriod", columnNames = {"employee", "period"})})
public class Payment {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @NotNull
    @UserExist
    private String employee;

    @NotNull
    @JsonFormat(pattern = "MM-yyyy")
    private LocalDate period;
    @Positive
    @NotNull
    private Long salary;

    @JsonGetter("salary")
    public String getSalaryAsString() {
        return "%d dollar(s) %d cent(s)".formatted(salary/100, salary%100);
    }
}
