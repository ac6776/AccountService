package account.utils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class LocalDateConverter {

    public static LocalDate parse(String value) {
        YearMonth yearMonth = YearMonth.parse(value, DateTimeFormatter.ofPattern("MM-yyyy"));
        return yearMonth.atDay(1);
    }
}
