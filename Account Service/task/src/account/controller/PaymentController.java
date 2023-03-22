package account.controller;

import account.domain.Payment;
import account.domain.User;
import account.messages.CustomErrorMessage;
import account.service.PaymentService;
import account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PaymentController {
    private PaymentService service;

    @Autowired
    public void setService(PaymentService service) {
        this.service = service;
    }

    //GET api/empl/payment - gives access to the payroll of an employee
    @GetMapping("/empl/payment")
    public ResponseEntity<?> getPayment(@AuthenticationPrincipal UserDetails details, Errors errors, HttpServletRequest request) {
        if (errors.hasFieldErrors()) {
            return ResponseEntity.badRequest()
                    .body(new CustomErrorMessage(LocalDateTime.now().toString(),
                            HttpStatus.BAD_REQUEST.value(),
                            HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            errors.getFieldError().getDefaultMessage(),
                            request.getServletPath()));
        }
        return ResponseEntity.ok(service.findAllPaymentsByEmail(details.getUsername()));
    }

    //POST api/acct/payments - uploads payrolls
    @PostMapping("/acct/payments")
    public ResponseEntity<?> addPayments(List<@Valid Payment> payments, Errors errors, HttpServletRequest request)  {
        if (errors.hasFieldErrors()) {
            return ResponseEntity.badRequest()
                    .body(new CustomErrorMessage(LocalDateTime.now().toString(),
                            HttpStatus.BAD_REQUEST.value(),
                            HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            errors.getFieldError().getDefaultMessage(),
                            request.getServletPath()));
        }
        return ResponseEntity.ok().body(service.saveAll(payments));
    }

    //PUT api/acct/payments - changes the salary of a specific user
    @PutMapping("/acct/payments")
    public ResponseEntity<?> updateSalary(@Valid Payment payment, Errors errors, HttpServletRequest request)  {
        if (errors.hasFieldErrors()) {
            return ResponseEntity.badRequest()
                    .body(new CustomErrorMessage(LocalDateTime.now().toString(),
                            HttpStatus.BAD_REQUEST.value(),
                            HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            errors.getFieldError().getDefaultMessage(),
                            request.getServletPath()));
        }
        return ResponseEntity.ok().body(service.save(payment));
    }
}
