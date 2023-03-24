package account.domain;

import account.utils.LocalDateConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
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
    private String employee;

    @NotNull
    private LocalDate period;

    @Positive(message = "value should be greater than 0")
    @NotNull
    private Long salary;

    @JsonSetter("period")
    public void setPeriod(String period) {
        this.period = LocalDateConverter.parse(period);
    }
}
