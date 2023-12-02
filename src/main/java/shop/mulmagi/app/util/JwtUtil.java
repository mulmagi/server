package shop.mulmagi.app.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import shop.mulmagi.app.dao.CustomUserDetails;
import shop.mulmagi.app.service.UserService;
import shop.mulmagi.app.web.dto.TokenDto;

import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {
    @Value("${spring.jwt.secretKey}")
    private String secret;

    @Value("${spring.jwt.access-expiration-time}")
    private long accessExpTime;

    @Value("${spring.jwt.refresh-expiration-time}")
    private long refreshExpTime;

    private UserService userService;


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

    private String generateToken(String phoneNumber, long expirationTime) {
        return Jwts.builder()
                .setSubject(phoneNumber)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
    public String generateAccessToken(CustomUserDetails userDetails) {
        return generateToken(userDetails.getUsername(), accessExpTime);
    }

    public String generateRefreshToken(CustomUserDetails userDetails) {
        return generateToken(userDetails.getUsername(), refreshExpTime);
    }


    public Boolean validateToken(String token, CustomUserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String recreateAccessToken(String refreshToken,CustomUserDetails userDetails) {
        if(validateToken(refreshToken, userDetails)){
            String username = extractUsername(refreshToken);
            userDetails = userService.loadUserByPhoneNumber(username);
        }
        return generateAccessToken(userDetails);
    }

}
