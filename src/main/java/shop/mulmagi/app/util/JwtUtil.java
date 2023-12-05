package shop.mulmagi.app.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import shop.mulmagi.app.dao.CustomUserDetails;
import shop.mulmagi.app.domain.RefreshToken;
import shop.mulmagi.app.domain.User;
import shop.mulmagi.app.exception.CustomExceptions;
import shop.mulmagi.app.repository.RefreshTokenRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Function;


@Slf4j
@Service
@RequiredArgsConstructor
public class JwtUtil {
    @Value("${spring.jwt.secretKey}")
    private String secret;

    @Value("${spring.jwt.access-expiration-time}")
    private long accessExpTime;

    @Value("${spring.jwt.refresh-expiration-time}")
    private long refreshExpTime;

    public Long extractId(String jwtToken) {
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(jwtToken).getBody();
        } catch ( JwtException | IllegalArgumentException e) {
            // 예외 처리 (예: 유효하지 않은 토큰 등)
            e.printStackTrace(); // 예외 처리 방식을 개선해야 함
        }

        if (claims != null) {
            return Long.parseLong(claims.getSubject());
        } else {
            // 토큰이 유효하지 않은 경우 처리 (예: 로깅, 예외 처리)
            return null;
        }
    }
    public long getRefreshExpTime() {
        return refreshExpTime;
    }

    private final RefreshTokenRepository refreshTokenRepository;


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateAccessToken(CustomUserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("tokenType", "access")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessExpTime))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String generateRefreshToken(CustomUserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("tokenType", "refresh")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpTime))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }


    public Boolean validateToken(String token, CustomUserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public void invalidateToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        if (claims.get("tokenType").equals("access")) {
            claims.setExpiration(new Date());
        } else if (claims.get("tokenType").equals("refresh")) {
            claims.setExpiration(new Date());
        }
    }
    public Date calculateRefreshExpirationTime() {
        long currentMillis = System.currentTimeMillis();
        return new Date(currentMillis + refreshExpTime);
    }

    public String generateAccessTokenFromRefreshToken(String refreshToken) {
        CustomUserDetails userDetails = getUserDetailsFromRefreshToken(refreshToken);

        // 유효한 유저 정보가 없거나 Refresh Token이 만료된 경우
        if (userDetails == null || isTokenExpired(refreshToken)) {
            return null;
        }

        // Access Token 생성
        return generateAccessToken(userDetails);
    }

    public CustomUserDetails getUserDetailsFromRefreshToken(String token) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token);

        // RefreshToken이 없거나 만료된 경우
        if (refreshToken == null || isTokenExpired(token)) {
            throw new CustomExceptions.RefreshTokenInvalidException("Refresh Token이 유효하지 않습니다.");
        }

        User user = refreshToken.getUser();
        return buildCustomUserDetails(user);
    }

    public CustomUserDetails buildCustomUserDetails(User user) {
        return CustomUserDetails.builder()
                .id(user.getId())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .isAdmin(user.getIsAdmin())
                .level(user.getLevel())
                .experience(user.getExperience())
                .point(user.getPoint())
                .profileUrl(user.getProfileUrl())
                .isRental(user.getIsRental())
                .status(user.getStatus())
                .isComplaining(user.getIsComplaining())
                .authorities(getAuthorities(user.getIsAdmin()))
                .build();
    }

    public Collection<GrantedAuthority> getAuthorities(boolean isAdmin) {
        List<GrantedAuthority> authorityList = new ArrayList<>();

        if (isAdmin) {
            authorityList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return authorityList;
    }
}
