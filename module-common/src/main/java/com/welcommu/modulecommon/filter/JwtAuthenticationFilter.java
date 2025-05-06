package com.welcommu.modulecommon.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.token.JwtTokenHelper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
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
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
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
        log.info("JWT token: {}", token);

        try {
            Map<String, Object> claims = jwtTokenHelper.validationTokenWithThrow(token);

            String type = (String) claims.get("tokenType");
            if (!"access".equals(type)) {
                log.warn("토큰 타입 불일치: {}", type);
                respondWithCustomError(response, CustomErrorCode.INVALID_REFRESH_TOKEN_TYPE);
                return;
            }

            String email = (String) claims.get("email");
            UserDetails user = userDetailsService.loadUserByUsername(email);

            log.info("인증 성공: {}", email);
            UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
            log.info(">>> Authentication set: {}", auth.getAuthorities());

        } catch (ExpiredJwtException e) {
            log.warn("JWT 만료됨", e);
            respondWithCustomError(response, CustomErrorCode.EXPIRED_TOKEN);
            return;
        } catch (JwtException e) {
            log.error("JWT 파싱 오류", e);
            respondWithCustomError(response, CustomErrorCode.INVALID_TOKEN);
            return;
        } catch (Exception e) {
            log.error("JWT 처리 중 예기치 않은 오류", e);
            respondWithCustomError(response, CustomErrorCode.SERVER_ERROR);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if ("accessToken".equals(c.getName())) {
                    return c.getValue();
                }
            }
        }
        return null;
    }

    private void respondWithCustomError(HttpServletResponse response, CustomErrorCode errorCode) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=UTF-8");

        Map<String, String> body = Map.of(
            "status", "error",
            "code", errorCode.getCode(),
            "message", errorCode.getErrorMessage()
        );

        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}