package com.welcommu.moduleapi.auth;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class JwtUserController {

    @GetMapping("/user")
    public String getUserInfo(@AuthenticationPrincipal String username) {
        // 사용자의 정보를 DB에서 가져와서 반환하는 로직을 추가
        return "Authenticated user: " + username;
    }
}
