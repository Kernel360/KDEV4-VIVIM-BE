package com.welcommu.modulecommon.util;

import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import org.springframework.http.ResponseCookie;

public class TokenCookieUtil {

    public static void addTokenCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", accessToken)
            .domain("localhost")
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(Duration.ofSeconds(10))
            .sameSite("None")
            .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
            .domain("localhost")
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(Duration.ofMinutes(1))
            .sameSite("None")
            .build();

        response.addHeader("Set-Cookie", accessCookie.toString());
        response.addHeader("Set-Cookie", refreshCookie.toString());
    }
}
