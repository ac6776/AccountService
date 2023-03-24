package account.controller;

import account.domain.Payment;
import account.domain.PaymentDTO;
import account.messages.CustomErrorMessage;
import account.service.PaymentService;
import account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/api")
public class PaymentController {
    private PaymentService paymentService;
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    //GET api/empl/payment - gives access to the payroll of an employee
    @GetMapping("/empl/payment")
    public ResponseEntity<?> getPayment(@AuthenticationPrincipal UserDetails userDetails) {
        var userFromDB = userService.findByEmail(userDetails.getUsername());
        var payments = paymentService.findAllPaymentsByEmail(userDetails.getUsername());
        var sortedPayments = payments.stream().sorted((a, b) -> {
            if (a.getPeriod() == b.getPeriod()) return 0;
            else if (a.getPeriod().after(b.getPeriod())) {
                return -1;
            }
            return 1;
        }).toList();
        return ResponseEntity.ok(sortedPayments.stream().map(p -> new PaymentDTO(
                userFromDB.getName(),
                userFromDB.getLastname(),
                p.getPeriod(),
                p.getSalary())).collect(Collectors.toList()));
    }

    //POST api/acct/payments - uploads payrolls
    @PostMapping("/acct/payments")
    public ResponseEntity<?> addPayments(@RequestBody List<@Valid Payment> payments)  {
        paymentService.saveAll(payments);
        return ResponseEntity.ok().body(Map.of("status", "Added successfully!"));
    }

    //PUT api/acct/payments - changes the salary of a specific user
    @PutMapping("/acct/payments")
    public ResponseEntity<?> updateSalary(@Valid Payment payment)  {
        return ResponseEntity.ok().body(paymentService.save(payment));
    }
}
