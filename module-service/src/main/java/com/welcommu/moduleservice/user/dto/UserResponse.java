package com.welcommu.moduleservice.user.dto;

import com.welcommu.moduledomain.user.User;

public class UserResponse {

    private Long id;
    private String email;
    private String name;

    public UserResponse(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getName());
    }
}
