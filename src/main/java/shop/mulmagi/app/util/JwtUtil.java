package shop.mulmagi.app.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import shop.mulmagi.app.dao.CustomUserDetails;

import java.util.Date;
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

    public long getRefreshExpTime() {
        return refreshExpTime;
    }


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


}
