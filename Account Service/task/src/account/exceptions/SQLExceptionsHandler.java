package account.exceptions;

import account.messages.CustomErrorMessage;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class SQLExceptionsHandler {

    @ExceptionHandler(JdbcSQLIntegrityConstraintViolationException.class)
    public ResponseEntity<?> handleJdbcSQLIntegrityConstraintViolationException(JdbcSQLIntegrityConstraintViolationException ex, WebRequest request) {
        return ResponseEntity.badRequest().body(new CustomErrorMessage(LocalDateTime.now().toString(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getLocalizedMessage(),
                request.getDescription(false).substring("uri=".length())));
    }
}
