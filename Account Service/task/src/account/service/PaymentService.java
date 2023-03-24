package account.service;

import account.domain.Payment;
import account.repository.PaymentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PaymentService {

    private PaymentsRepository paymentsRepository;
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setPaymentsRepository(PaymentsRepository paymentsRepository) {
        this.paymentsRepository = paymentsRepository;
    }

    @Transactional
    public boolean saveAll(List<Payment> payments) {
        payments.stream().map(p -> userService.findByEmail(p.getEmployee()));
        return !paymentsRepository.saveAll(payments).isEmpty();
    }

    @Transactional
    public List<Payment> findAllPaymentsByEmail(String email) {
        return paymentsRepository.findAllPaymentsByEmployee(email);
    }

    @Transactional
    public Payment save(Payment payment) {
        return paymentsRepository.save(payment);
    }
}
