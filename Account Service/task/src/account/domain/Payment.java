package account.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Date;

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
//    @Pattern(regexp = "(0[1-9]|1[0-2])-20([0-1][0-9]|2[0-3])", message = "date should be [mm-yyyy]")
    @JsonFormat(pattern = "MM-yyyy")
    private Date period;
    @Positive(message = "value should be greater than 0")
    @NotNull
    private Long salary;


//    public void setPeriod(String period) {
//        LocalDate date = LocalDate.parse(period);
//        this.period = new Date();
//    }
}
