package com.welcommu.modulecommon.filter;

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

    private static final String[] SWAGGER_WHITELIST = {
        "/swagger-ui", "/swagger-ui/", "/swagger-ui.html", "/swagger-ui/index.html", "/v3/api-docs"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        for (String swaggerPath : SWAGGER_WHITELIST) {
            if (requestURI.startsWith(swaggerPath)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        Set<String> excludedPaths = Set.of("/api/auth/refresh-token", "/api/auth/login", "/api/users/resetpassword");

        if (excludedPaths.contains(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getTokenFromRequest(request);

        if (token == null) {
            log.warn("Authorization 헤더가 없습니다.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authorization 헤더가 없습니다.");
            return;
        }

        try {
            Map<String, Object> claims = jwtTokenHelper.validationTokenWithThrow(token);

            // Access Token 타입만 허용
            String tokenType = (String) claims.get("tokenType");
            if (!"access".equals(tokenType)) {
                log.warn("Invalid token type: {}", tokenType);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid token type");
                return;
            }

            String username = (String) claims.get("email");
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            log.info("유효한 JWT 토큰으로 인증된 사용자: {}", username);

            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null,
                    userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            log.error("JWT 처리 중 오류가 발생했습니다.", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("유효하지 않은 JWT 토큰입니다.");
            return;
        }

        // 다음 필터 실행
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}