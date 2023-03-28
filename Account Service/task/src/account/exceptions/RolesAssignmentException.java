package account.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class RolesAssignmentException extends RuntimeException {
    public RolesAssignmentException(String message) {
        super(message);
    }
}
