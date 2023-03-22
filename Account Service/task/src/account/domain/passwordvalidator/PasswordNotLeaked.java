package account.domain.passwordvalidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = { PasswordLeakValidator.class })
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordNotLeaked {
    String message() default "Password leaked";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}