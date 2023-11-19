package shop.mulmagi.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.mulmagi.app.domain.Location;
import shop.mulmagi.app.domain.UmbrellaStand;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query("SELECT lo FROM Location lo " +
            "WHERE FUNCTION('ST_Distance_Sphere', POINT(:userLongitude, :userLatitude), POINT(lo.longitude, lo.latitude)) < :distanceThreshold " +
            "AND (lo.umbrellaCount - lo.wrongCount) > 0")
    List<Location> findNearbyLocationsByIsUmbrella(@Param("userLatitude") Double userLatitude, @Param("userLongitude") Double userLongitude, @Param("distanceThreshold") Double distanceThreshold);

    @Query("SELECT lo FROM Location lo " +
            "WHERE FUNCTION('ST_Distance_Sphere', POINT(:userLongitude, :userLatitude), POINT(lo.longitude, lo.latitude)) < :distanceThreshold " +
            "AND (9 - lo.umbrellaCount) > 0")
    List<Location> findNearbyLocationsByAvailable(@Param("userLatitude") Double userLatitude, @Param("userLongitude") Double userLongitude, @Param("distanceThreshold") Double distanceThreshold);


}
