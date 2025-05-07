package com.welcommu.modulecommon.util;

import com.welcommu.modulecommon.token.JwtProperties;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class JwtUtil {

    private final JwtProperties props;

    public void addTokenCookies(HttpServletResponse response, String accessToken,
        String refreshToken) {

        ResponseCookie accessCookie = ResponseCookie.from("accessToken", accessToken)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(props.getAccessTokenTtl())
            .sameSite("None")
            .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(props.getRefreshTokenTtl())
            .sameSite("None")
            .build();

        response.addHeader("Set-Cookie", accessCookie.toString());
        response.addHeader("Set-Cookie", refreshCookie.toString());
    }
}
