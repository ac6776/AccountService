package account.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "The passwords must be different!")
public class PasswordAlreadyInUseException extends RuntimeException {
}
