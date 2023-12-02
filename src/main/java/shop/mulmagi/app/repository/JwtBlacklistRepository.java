package shop.mulmagi.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shop.mulmagi.app.domain.JwtBlacklist;

import javax.transaction.Transactional;

@Repository
public interface JwtBlacklistRepository extends JpaRepository<JwtBlacklist, Long> {

    JwtBlacklist findByToken(String token);

    boolean existsByToken(String token);

    void deleteByToken(String token);
}
