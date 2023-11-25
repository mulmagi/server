package shop.mulmagi.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import shop.mulmagi.app.domain.Message;
import shop.mulmagi.app.domain.User;

public interface MessageRepository extends JpaRepository<Message, Long> {
	List<Message> findByUser(User user);
	Message findTopByUserOrderByCreatedAtDesc(User user);
}
