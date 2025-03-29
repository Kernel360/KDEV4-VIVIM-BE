package com.welcommu.modulecommon.token;


import com.welcommu.modulecommon.security.JwtUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TokenInitializer {

    @Autowired
    private JwtUtil jwtUtil;

    @PostConstruct
    public void init() {
        String adminToken = jwtUtil.generateToken("admin"); // 관리자 토큰 생성
        System.out.println("서버 시작 시 생성된 관리자 토큰: " + adminToken);
    }
}