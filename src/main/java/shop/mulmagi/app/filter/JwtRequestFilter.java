package shop.mulmagi.app.filter;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import shop.mulmagi.app.dao.CustomUserDetails;
import shop.mulmagi.app.security.CustomAuthenticationProvider;
import shop.mulmagi.app.service.UserService;
import shop.mulmagi.app.util.JwtUtil;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import lombok.extern.slf4j.Slf4j;




@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final CustomAuthenticationProvider customAuthenticationProvider;


    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String userId = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            userId = jwtUtil.extractId(jwt);

            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                try {
                    // JWT 토큰의 유효성을 검사하여 인증 정보를 생성
                    if (jwtUtil.validateToken(jwt,userService.loadUserById(Long.parseLong(userId)))) {
                        Authentication authentication = customAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(Long.parseLong(userId), null));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } catch (Exception e) {
                    log.error("JWT token validation failed: {}", e.getMessage());
                }
            }
            filterChain.doFilter(request, response);
        }
    }
}
