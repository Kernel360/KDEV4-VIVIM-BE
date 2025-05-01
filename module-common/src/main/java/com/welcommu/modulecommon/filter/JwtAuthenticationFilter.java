package com.welcommu.modulecommon.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.welcommu.modulecommon.token.helper.JwtTokenHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenHelper jwtTokenHelper;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;  // Jackson ObjectMapper 주입

    private static final String[] SWAGGER_WHITELIST = {
        "/swagger-ui", "/swagger-ui/", "/swagger-ui.html", "/swagger-ui/index.html", "/v3/api-docs"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain)
        throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        for (String swaggerPath : SWAGGER_WHITELIST) {
            if (requestURI.startsWith(swaggerPath)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        Set<String> excludedPaths = Set.of(
            "/api/auth/refresh-token",
            "/api/auth/login",
            "/api/users/resetpassword"
        );
        if (excludedPaths.contains(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getTokenFromRequest(request);
        if (token == null) {
            log.warn("Authorization 헤더가 없습니다.");
            respondUnauthorized(response, "Authorization 헤더가 없습니다.");
            return;
        }

        try {
            Map<String, Object> claims = jwtTokenHelper.validationTokenWithThrow(token);

            // Access Token 타입만 허용
            String tokenType = (String) claims.get("tokenType");
            if (!"access".equals(tokenType)) {
                log.warn("토큰 타입 불일치: {}", tokenType);
                respondUnauthorized(response,
                    "허용되지 않은 토큰 타입입니다. 액세스 토큰만 사용할 수 있습니다.");
                return;
            }

            String username = (String) claims.get("email");
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            log.info("유효한 JWT 토큰으로 인증된 사용자: {}", username);
            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
                );
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            log.error("JWT 처리 중 오류가 발생했습니다.", e);
            respondUnauthorized(response, "유효하지 않은 JWT 토큰입니다.");
            return;
        }

        // 다음 필터 실행
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        return (header != null && header.startsWith("Bearer "))
            ? header.substring(7)
            : null;
    }

    private void respondUnauthorized(HttpServletResponse response, String message)
        throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=UTF-8");
        Map<String, String> body = Map.of(
            "status", "error",
            "message", message
        );
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}