package account.domain.passwordvalidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.regex.Pattern;

public class PasswordLeakValidator implements ConstraintValidator<PasswordNotLeaked, String> {

    private final List<String> corruptedPasswords = List.of("PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
            "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
            "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember");
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        List<Pattern> patterns = corruptedPasswords.stream().map(Pattern::compile).toList();
        for (Pattern pattern: patterns) {
            if (pattern.matcher(value).matches())
                return false;
        }
        return true;
    }
}
