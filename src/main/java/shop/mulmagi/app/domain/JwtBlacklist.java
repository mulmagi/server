package shop.mulmagi.app.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "jwt_blacklist")
public class JwtBlacklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", unique = true)
    private String token;
}
