package shop.mulmagi.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.mulmagi.app.domain.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserId(Long id);
}
