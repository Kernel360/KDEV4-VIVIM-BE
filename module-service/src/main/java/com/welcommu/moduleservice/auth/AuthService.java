package com.welcommu.moduleservice.auth;

import com.welcommu.moduleservice.auth.dto.LoginRequest;
import com.welcommu.moduleservice.auth.dto.LoginResponse;

public interface AuthService {
    LoginResponse createToken(LoginRequest request);
    LoginResponse reIssueToken(String refreshToken);
    void deleteToken(String refreshToken);
}
