package shop.mulmagi.app.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import shop.mulmagi.app.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByPhoneNumber(String phoneNumber);

    List<User> findByIsComplaining(Boolean isComplaining);
}
