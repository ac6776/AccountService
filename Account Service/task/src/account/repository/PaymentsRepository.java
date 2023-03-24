package account.repository;

import account.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentsRepository extends JpaRepository<Payment, Long> {
    List<Payment> findAllPaymentsByEmployeeIgnoreCase(String employee); //email
    Optional<Payment> findOneByPeriodAndEmployeeIgnoreCase(LocalDate period, String employee);
}
