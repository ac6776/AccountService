package account.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentDTO {
    private String name;
    private String lastname;
    private LocalDate period;
    private Long salary;

    @JsonGetter("salary")
    public String getSalaryAsString() {
        return "%d dollar(s) %d cent(s)".formatted(salary/100, salary%100);
    }

    @JsonGetter("period")
    public String getNamedMonths() {
        return period.format(DateTimeFormatter.ofPattern("MMMM-yyyy", Locale.ENGLISH));
    }
}
