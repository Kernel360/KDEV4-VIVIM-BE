package com.welcommu.moduleservice.logging.dto;

import com.welcommu.moduledomain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSnapshot {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String companyName;
    private String companyRole;

    public static UserSnapshot from(User user) {
        return new UserSnapshot(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPhone(),
            user.getCompany() != null ? user.getCompany().getName() : null,
            user.getCompany() != null ? user.getCompany().getCompanyRole().name() : null
        );
    }
}