package com.welcommu.moduleapi.user;

import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // 인증 로직 구현
        // 성공시 사용자 정보 반환
        return ResponseEntity.ok()
                .body(Map.of("message", "Login successful"));
    }

    @Data
    public class LoginRequest {
        private String username;
        private String password;
    }
}

