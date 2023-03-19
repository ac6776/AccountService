package account.security.passwordvalidator;

import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;
import org.hibernate.validator.constraints.Length;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

@Documented
@ConstraintComposition(CompositionType.AND)
@Constraint(validatedBy = { })
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@NotNull
@Length(min = 12, message = "Password length must be 12 chars minimum!")
@PasswordNotLeaked(message = "The password is in the hacker's database!")
public @interface Password {
    String message() default "Password validation failed";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
