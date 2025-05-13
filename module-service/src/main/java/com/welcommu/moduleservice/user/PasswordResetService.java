package com.welcommu.moduleservice.user;

public interface PasswordResetService {
    void requestReset(String email);
    boolean confirmReset(String email, String code, String newPassword);
}
