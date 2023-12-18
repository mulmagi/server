package shop.mulmagi.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.mulmagi.app.domain.Rental;
import shop.mulmagi.app.domain.UmbrellaStand;
import shop.mulmagi.app.domain.User;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    @Query("SELECT r FROM Rental r WHERE r.user.id = :userId AND r.returnUmbrellaStand IS NOT NULL")
    List<Rental> findByUserIdAndReturnUmbrellaStandNotNull(@Param("userId") Long userId);
    List<Rental> findByUserId(Long userId);
    List<Rental> findByIsReturn(Boolean isReturn);
    Rental findByUserAndIsReturn(User user, Boolean isReturn);
    Rental findByReturnUmbrellaStand(UmbrellaStand umbrellaStand);

}
