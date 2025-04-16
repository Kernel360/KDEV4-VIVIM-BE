package com.welcommu.modulecommon.filter;

import com.welcommu.modulecommon.security.CustomUserDetailsService;
import com.welcommu.modulecommon.token.helper.JwtTokenHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer.UserDetailsBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenHelper jwtTokenHelper;
    private final CustomUserDetailsService userDetailsService;

    private static final String[] SWAGGER_WHITELIST = {
            "/swagger-ui", "/swagger-ui/", "/swagger-ui.html", "/swagger-ui/index.html", "/v3/api-docs"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        // Swagger UI 및 API 문서 관련 요청은 JWT 필터를 적용하지 않음
        for (String swaggerPath : SWAGGER_WHITELIST) {
            if (requestURI.startsWith(swaggerPath)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        // 로그인 요청은 JWT 필터를 건너뛰어야 하므로 처리
        if ("/api/auth/login".equals(request.getRequestURI()) || "/api/users/resetpassword".equals(request.getRequestURI())) {
            filterChain.doFilter(request, response); // 토큰 검사 없이 다음 필터로 진행
            return;
        }

        // Authorization 헤더에서 JWT 토큰을 추출
        String token = getTokenFromRequest(request);

        // Authorization 헤더가 없을 경우 처리
        if (token == null) {
            logger.warn("Authorization 헤더가 없습니다.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
            response.getWriter().write("Authorization header is missing");
            return;
        }

        // 토큰이 유효한지 확인하고 인증 정보 설정
        try {
            Map<String, Object> claims = jwtTokenHelper.validationTokenWithThrow(token);

            // 유효한 토큰이 있을 때 로그
            String username = (String) claims.get("email");
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            logger.info("유효한 JWT 토큰으로 인증된 사용자: " + username);

            // 인증 정보 설정
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            logger.error("JWT 처리 중 오류 발생", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
            response.getWriter().write("Invalid JWT token");
            return;  // 더 이상 필터 체인 진행하지 않음
        }

        // 필터 체인 계속 진행
        filterChain.doFilter(request, response);
    }

    // Authorization 헤더에서 JWT 토큰을 추출
    private String getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7); // "Bearer " 부분을 제거하고 토큰만 추출
        }
        return null;
    }
}