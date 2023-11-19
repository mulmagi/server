package shop.mulmagi.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.mulmagi.app.domain.Rental;
import shop.mulmagi.app.domain.User;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByIsReturn(Boolean isReturn);
    Rental findByUserAndIsReturn(User user, Boolean isReturn);
}
