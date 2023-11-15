package shop.mulmagi.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.mulmagi.app.domain.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}