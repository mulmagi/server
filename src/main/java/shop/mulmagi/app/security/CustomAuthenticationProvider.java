package shop.mulmagi.app.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import shop.mulmagi.app.dao.CustomUserDetails;
import shop.mulmagi.app.domain.User;
import shop.mulmagi.app.exception.CustomExceptions;
import shop.mulmagi.app.service.UserService;
import shop.mulmagi.app.util.JwtUtil;

import java.util.Arrays;
import java.util.List;
@Component
@Configuration
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private static final List<String> PERMITTED_ENDPOINTS = Arrays.asList("/name", "/sms-certification/send","/sms-certification/confirm");

    private final UserService userService;

    private final JwtUtil jwtUtil;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String token = (String) authentication.getCredentials();

        // 주어진 Token의 유효성을 확인하고, 사용자 정보를 가져옴
        CustomUserDetails userDetails = jwtUtil.getUserDetailsFromRefreshToken(token);

        Long userId = (Long)authentication.getPrincipal();

        User user = userService.findById(userId);

        if (user != null && userDetails == jwtUtil.buildCustomUserDetails(user)) {
            return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
        }

        throw new CustomExceptions.UserNotFoundException("사용자를 찾을 수 없습니다.");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    private boolean isPermittedEndpoint(String requestURI) {
        return PERMITTED_ENDPOINTS.contains(requestURI);
    }
}
