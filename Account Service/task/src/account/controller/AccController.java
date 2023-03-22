package account.controller;

import account.domain.Payment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping
public class AccController {

    @PostMapping
    public ResponseEntity<?> addPayments(List<@Valid Payment> payments)  {
        return ResponseEntity.ok().body("");
    }
}
