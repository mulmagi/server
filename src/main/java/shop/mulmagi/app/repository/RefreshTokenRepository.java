package shop.mulmagi.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.mulmagi.app.domain.RefreshToken;
import shop.mulmagi.app.domain.User;
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByUser(User user);

    RefreshToken findByToken(String token);

    void deleteByUser(User user);
}
