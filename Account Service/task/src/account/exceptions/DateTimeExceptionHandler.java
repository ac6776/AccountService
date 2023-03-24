package account.exceptions;

import account.messages.CustomErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.DateTimeException;
import java.time.LocalDateTime;

@ControllerAdvice
public class DateTimeExceptionHandler {

    @ExceptionHandler(DateTimeException.class)
    public ResponseEntity<CustomErrorMessage> handleDateTimeException(DateTimeException ex, WebRequest request) {
        return ResponseEntity.badRequest().body(new CustomErrorMessage(LocalDateTime.now().toString(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getLocalizedMessage(),
                request.getDescription(false).substring("uri=".length())));
    }
}
