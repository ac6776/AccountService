package account.domain.uservalidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = { UserExistValidator.class })
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserExist {
    String message() default "User not found";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
