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

import java.util.*;
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
    private final RefreshTokenRepository refreshTokenRepository;


    // jwt토큰의 claim 중 하나인 subject로 지정되는 User의id를 추출하는 함수
    public Long extractId(String token) {
        return Long.valueOf(extractClaim(token, Claims::getSubject));
    }

    // jwt토큰의 claim 중 하나인 Expiration으로 지정되는 만료 시간 추출하는 함수
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    //jwt 토큰에서 특정 클레임만 추출하는 함수
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // jwt 토큰에서 모든 클레임을 추출하는 함수
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    // token이 만료되었는지 확인하는 함수
    // token의 만료 시간을 가져온 후, 현재 시간과 비교해 토큰의 만료 여부를 return
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // accessToken 발급 함수
    // subject로 지정되는 사용자 이름은 휴대폰 번호임
    //tokenType은 access, 지금 시간으로 발급, accessExpTime에 만료됨.
    public String generateAccessToken(Long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("tokenType", "access")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessExpTime))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    //refreshToken 발급 함수
    // subject로 지정되는 사용자 이름은 User의 id임
    //tokenType은 refresh, 지금 시간으로 발급, refreshExpTime에 만료됨.
    public String generateRefreshToken(Long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("tokenType", "refresh")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpTime))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }


    // 토큰이 유효한지 검사하는 함수
    // userDetails.getUsername은 User의 id를 return함
    public Boolean validateToken(String token, CustomUserDetails userDetails) {
        final Long user_id = extractId(token);
        return (user_id.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // token들의 만료 시간을 지금으로 설정해서 토큰을 무효화시키는 함수
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

    public CustomUserDetails getUserDetailsFromRefreshToken(String token) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token);

        if (refreshToken == null || isTokenExpired(token)) {
            throw new CustomExceptions.RefreshTokenInvalidException("Refresh Token이 유효하지 않습니다.");
        }

        User user = refreshToken.getUser();
        return buildCustomUserDetails(user);
    }

    public String generateAccessTokenFromRefreshToken(String refreshToken) {
        CustomUserDetails userDetails = getUserDetailsFromRefreshToken(refreshToken);
        Long id = extractId(refreshToken);

        if (userDetails == null || isTokenExpired(refreshToken)) {
            throw new CustomExceptions.RefreshTokenInvalidException("Refresh Token이 유효하지 않습니다.");
        }
        return generateAccessToken(id);
    }


// User 엔티티를 CustomUserDetails로 바꿔주는 함수
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
                .firebaseToken(user.getFirebaseToken())
                .agreeTerms(user.isAgreeTerms())
                .notificationEnabled(user.isNotificationEnabled())
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
