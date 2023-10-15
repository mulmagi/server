package shop.mulmagi.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.mulmagi.app.domain.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
