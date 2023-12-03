package shop.mulmagi.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import shop.mulmagi.app.domain.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByPhoneNumber(String phoneNumber);

    List<User> findByIsComplaining(Boolean isComplaining);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.firebaseToken = :firebaseToken WHERE u.id = :id")
    void updateFirebaseToken(@Param("id") Long id, @Param("firebaseToken")String firebaseToken);

}
