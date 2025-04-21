package com.welcommu.moduleservice.user.dto;

import com.welcommu.moduledomain.company.Company;
import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.user.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
public class UserModifyRequest {
    @NotNull
    private String name;
    @Email
    private String email;

    @NotNull
    private String phone;

    @NotNull
    private Long companyId;

    public User modifyUser(User user,Company company) {
        user.setName(this.name);
        user.setEmail(this.email);
        user.setPhone(this.phone);
        user.setCompany(company);
        user.setModifiedAt(LocalDateTime.now());
        return user;
    }
}