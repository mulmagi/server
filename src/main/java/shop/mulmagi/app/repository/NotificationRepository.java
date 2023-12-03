package shop.mulmagi.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import shop.mulmagi.app.domain.Notification;
import shop.mulmagi.app.domain.User;
import shop.mulmagi.app.domain.enums.NotificationType;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(Long id);

    @Query("SELECT n FROM Notification n " +
            "WHERE n.user = :user AND n.type IN :rentalList " +
            "ORDER BY n.createdAt DESC")
    List<Notification> findRentalByUserId(User user, List<NotificationType> rentalList);

    @Query("SELECT n FROM Notification n " +
            "WHERE n.user = :user AND n.type IN :pointList " +
            "ORDER BY n.createdAt DESC")
    List<Notification> findPointByUserId(User user, List<NotificationType> pointList);

    @Query("SELECT n FROM Notification n " +
            "WHERE n.user = :user AND n.type IN :etcList " +
            "ORDER BY n.createdAt DESC")
    List<Notification> findEtcByUserId(User user, List<NotificationType> etcList);

}
