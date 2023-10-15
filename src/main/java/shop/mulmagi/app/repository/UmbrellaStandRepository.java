package shop.mulmagi.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.mulmagi.app.domain.Location;
import shop.mulmagi.app.domain.UmbrellaStand;

import java.util.List;

public interface UmbrellaStandRepository extends JpaRepository<UmbrellaStand, Long> {

    @Query("SELECT us.number FROM UmbrellaStand us " +
            "WHERE us.location.id = :locationId AND us.isWrong = :isWrong")
    List<Integer> findNumbersByLocationAndIsWrong(Long locationId, boolean isWrong);

    UmbrellaStand findByQrCode(Long qrCode);
}
