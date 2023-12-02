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


    public String extractRefreshToken(String accessToken) {
        // AccessToken 파싱
        Claims claims = extractAllClaims(accessToken);

        // RefreshToken 추출
        return (String) claims.get("refreshToken");
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

//    public String recreateAccessToken(String refreshToken,CustomUserDetails userDetails) {
//        if(validateToken(refreshToken, userDetails)){
//            String username = extractUsername(refreshToken);
//            userDetails = userService.loadUserByPhoneNumber(username);
//        }
//        return generateAccessToken(userDetails);
//    }


}
