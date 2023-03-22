package account.service;

import account.domain.Payment;
import account.repository.PaymentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PaymentService {

    private PaymentsRepository paymentsRepository;

    @Autowired
    public void setPaymentsRepository(PaymentsRepository paymentsRepository) {
        this.paymentsRepository = paymentsRepository;
    }

    @Transactional
    public boolean saveAll(List<Payment> payments) {
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
