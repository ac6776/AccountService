package account.controller;

import account.domain.Payment;
import account.domain.PaymentDTO;
import account.security.UserDetailsImpl;
import account.service.PaymentService;
import account.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/api")
public class PaymentController {
    private PaymentService paymentService;
    private UserService userService;

    Logger logger = LoggerFactory.getLogger(this.getClass());

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
    public ResponseEntity<?> getPayment(@AuthenticationPrincipal UserDetails userDetails, @RequestParam(required = false) String period) {
        logger.info(userDetails.toString());
        logger.info(String.valueOf(((UserDetailsImpl)userDetails).getUser().isLocked()));

        var userFromDB = userService.findByEmail(userDetails.getUsername());
        if (period != null) {
            var paymentFromBD = paymentService.findOneByPeriodAndEmployee(period, userFromDB.getEmail());
            return ResponseEntity.ok(new PaymentDTO( userFromDB.getName(),
                    userFromDB.getLastname(),
                    paymentFromBD.getPeriod(),
                    paymentFromBD.getSalary()));
        }
        var payments = paymentService.findAllPaymentsByEmail(userDetails.getUsername());
        var sortedPayments = payments.stream().sorted(Comparator.comparing(Payment::getPeriod).reversed()).toList();
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
    public ResponseEntity<?> updateSalary(@RequestBody @Valid Payment payment)  {
        paymentService.save(payment);
        return ResponseEntity.ok().body(Map.of("status", "Updated successfully!"));
    }
}
