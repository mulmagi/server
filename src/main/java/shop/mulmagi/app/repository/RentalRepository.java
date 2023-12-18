package shop.mulmagi.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.mulmagi.app.domain.Rental;
import shop.mulmagi.app.domain.UmbrellaStand;
import shop.mulmagi.app.domain.User;

import java.nio.channels.FileChannel;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    List<Rental> findByUserId(Long userId);

    List<Rental> findByIsReturn(Boolean isReturn);

    Rental findByUserAndIsReturn(User user, Boolean isReturn);

    Rental findByReturnUmbrellaStand(UmbrellaStand umbrellaStand);

    @Query("SELECT r FROM Rental r WHERE r.user.id = :userId AND r.createdAt < :cursor AND r.returnUmbrellaStand IS NOT NULL ORDER BY r.createdAt DESC")
    List<Rental> findByUserIdAndCreatedAtBeforeAndNotNullReturnUmbrellaStand(Long userId, LocalDateTime cursor, Pageable pageable);
    Optional<Rental> findTop1ByUserIdAndCreatedAtBeforeOrderByCreatedAtDesc(Long userId, LocalDateTime createdAt);
}