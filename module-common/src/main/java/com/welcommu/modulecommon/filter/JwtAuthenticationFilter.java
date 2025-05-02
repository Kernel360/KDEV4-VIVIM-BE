package com.welcommu.modulecommon.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.welcommu.modulecommon.token.JwtTokenHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenHelper jwtTokenHelper;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    private static final String[] SWAGGER_WHITELIST = {
        "/swagger-ui", "/swagger-ui/", "/swagger-ui.html", "/swagger-ui/index.html", "/v3/api-docs"
    };

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        String uri = request.getRequestURI();
        for (String path : SWAGGER_WHITELIST) {
            if (uri.startsWith(path)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        Set<String> excluded = Set.of(
            "/api/auth/login",
            "/api/auth/refresh-token",
            "/api/users/resetpassword"
        );
        if (excluded.contains(uri)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getTokenFromRequest(request);
        log.info(token);
        if (token == null) {
            log.warn("Authorization 헤더·쿠키 토큰 없음");
            respondUnauthorized(response, "Authorization 헤더 또는 accessToken 쿠키가 필요합니다.");
            return;
        }

        try {
            Map<String, Object> claims = jwtTokenHelper.validationTokenWithThrow(token);

            // accessToken 타입만 허용
            String type = (String) claims.get("tokenType");
            if (!"access".equals(type)) {
                log.warn("토큰 타입 불일치: {}", type);
                respondUnauthorized(response, "허용되지 않은 토큰 타입입니다. 액세스 토큰만 사용할 수 있습니다.");
                return;
            }

            String email = (String) claims.get("email");
            UserDetails user = userDetailsService.loadUserByUsername(email);

            log.info("인증 성공: {}", email);
            UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (Exception e) {
            log.error("JWT 처리 중 오류", e);
            respondUnauthorized(response, "유효하지 않은 JWT 토큰입니다.");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if ("accessToken".equals(c.getName())) {
                    return c.getValue();
                }
            }
        }
        return null;
    }

    private void respondUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=UTF-8");
        Map<String, String> body = Map.of(
            "status", "error",
            "message", message
        );
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}