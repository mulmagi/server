package shop.mulmagi.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.mulmagi.app.domain.Rental;

public interface RentalRepository extends JpaRepository<Rental, Long> {
}
