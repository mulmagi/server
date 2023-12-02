package shop.mulmagi.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.mulmagi.app.domain.JwtBlacklist;

@Repository
public interface JwtBlacklistRepository extends JpaRepository<JwtBlacklist, Long> {

    JwtBlacklist findByToken(String token);

    void deleteByToken(String token);
}
