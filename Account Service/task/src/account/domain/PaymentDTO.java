package account.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentDTO {
    private String name;
    private String lastname;
    private Date period;
    private Long salary;

    @JsonGetter("salary")
    public String getSalaryAsString() {
        return "%d dollar(s) %d cent(s)".formatted(salary/100, salary%100);
    }

    @JsonGetter("period")
    public String getNamedMonths() {
        return new SimpleDateFormat("MMMM-yyyy").format(period);
    }
}
