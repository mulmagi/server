package shop.mulmagi.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.mulmagi.app.domain.Location;
import shop.mulmagi.app.domain.UmbrellaStand;

import java.util.List;

public interface UmbrellaStandRepository extends JpaRepository<UmbrellaStand, Long> {
    @Query("SELECT us.number FROM UmbrellaStand us " +
            "WHERE us.location.id = :locationId AND us.isWrong = false AND us.isUmbrella = true")
    List<Integer> findNumbersByLocationAndIsWrongAndIsUmbrella(Long locationId);

    @Query("SELECT us.number FROM UmbrellaStand us " +
            "WHERE us.location.id = :locationId AND us.isUmbrella = false")
    List<Integer> findNumbersByLocationAndIsUmbrella(Long locationId);

    UmbrellaStand findByQrCode(Long qrCode);
}
