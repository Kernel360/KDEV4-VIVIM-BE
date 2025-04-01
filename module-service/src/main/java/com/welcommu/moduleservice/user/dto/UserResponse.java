package com.welcommu.moduleservice.user.dto;

import com.welcommu.moduledomain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserResponse {

    private Long id;
    private String email;
    private String name;
    private String companyName;  // 회사 이름을 추가

    // User 객체를 UserResponse로 변환하는 정적 메서드
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getCompany() != null ? user.getCompany().getName() : null  // 회사 이름을 가져옴
        );
    }
}
