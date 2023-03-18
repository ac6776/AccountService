//package account.controller;
//
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//import java.time.LocalDateTime;
//
//@RestControllerAdvice
//public class AuthenticationExceptionHandler {
//
////    @ExceptionHandler({AuthenticationException.class})
////    public ResponseEntity<CustomErrorMessage> handleAuthenticationException(Exception ex, WebRequest request) throws Exception {
//////        var a = handleException(ex, request);
//////        System.out.println(a.getBody());
////        CustomErrorMessage body = new CustomErrorMessage(
////                LocalDateTime.now(),
////                HttpStatus.UNAUTHORIZED.value(),
////                ex.getMessage(),
////                "",
////                request.getDescription(false)
////        );
////        return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
////    }
//    @ExceptionHandler({AuthenticationException.class})
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    public ResponseEntity<CustomErrorMessage> handleAuthenticationException(AuthenticationException ex) {
//        return new ResponseEntity<>(, HttpStatus.UNAUTHORIZED);
//    }
//}
