package shop.mulmagi.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import shop.mulmagi.app.domain.Payment;
import shop.mulmagi.app.domain.User;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query(value = "SELECT amount, '충전' as transactionType, created_at FROM payment WHERE user_id = :user " +
            "UNION ALL " +
            "SELECT -1000 as amount, '대여료' as transactionType, created_at FROM rental WHERE user_id = :user " +
            "UNION ALL " +
            "SELECT -9000 as amount, '보증금' as transactionType, created_at FROM rental WHERE user_id = :user " +
            "UNION ALL " +
            "SELECT 9000 - overdue_amount as amount, '보증금 반환' as transactionType, updated_at FROM rental WHERE user_id = :user and is_return and overdue_amount < 9000 "+
            "ORDER BY created_at DESC, amount ASC", nativeQuery = true)
    List findAmountAndCreatedAtByUserId(User user);
}