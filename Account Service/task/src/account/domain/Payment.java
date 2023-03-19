package account.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Positive;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "payments", uniqueConstraints = {
        @UniqueConstraint(name = "UniqueEmployeeAndPeriod", columnNames = {"employee", "period"})})
public class Payment {
    @Id
    private Long id;
    private String employee;
    private String period;
    @Positive
    private String salary;

}
