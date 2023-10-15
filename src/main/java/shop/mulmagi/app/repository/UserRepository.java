package shop.mulmagi.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.mulmagi.app.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByPhoneNumber(String phoneNumber);
}
