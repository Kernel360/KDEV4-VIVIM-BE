package com.welcommu.modulecommon.config;

import com.welcommu.modulecommon.security.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

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
            if (token != null && jwtUtil.validateToken(token)) {
                String username = jwtUtil.extractUsername(token);

                // 유효한 토큰이 있을 때 로그
                logger.info("유효한 JWT 토큰으로 인증된 사용자: " + username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, null);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else if (token != null && jwtUtil.isTokenExpired(token)) {
                // 만료된 토큰 처리
                logger.warn("JWT 토큰이 만료되었습니다.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
                response.getWriter().write("JWT token has expired");
                return;
            } else {
                // 유효하지 않은 토큰에 대한 경고
                logger.warn("유효하지 않은 JWT 토큰입니다.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
                response.getWriter().write("Invalid or expired JWT token");
                return;
            }
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
