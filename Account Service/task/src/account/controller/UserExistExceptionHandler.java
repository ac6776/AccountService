package account.controller;

import account.config.UserExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

//@ControllerAdvice
//public class UserExistExceptionHandler {
//
//    @ExceptionHandler(UserExistException.class)
//    public ResponseEntity<CustomErrorMessage> handleUserExistException(UserExistException e, WebRequest request) {
//        CustomErrorMessage body = new CustomErrorMessage(
//                LocalDateTime.now(),
//                HttpStatus.BAD_REQUEST.value(),
//                HttpStatus.BAD_REQUEST.getReasonPhrase(),
//                e.getCause().getMessage(),
//                request.getDescription(false)
//        );
//        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
//    }
//
//}
