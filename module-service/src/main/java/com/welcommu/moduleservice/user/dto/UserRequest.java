package com.welcommu.moduleservice.user.dto;

import com.welcommu.moduledomain.company.Company;
import com.welcommu.moduledomain.user.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
public class UserRequest {
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Size(min = 4)
    private String password;

    @NotNull
    private String name;

    @NotNull
    private String phone;

    @NotNull
    private Long companyId;

    public User toEntity(Company company, PasswordEncoder encoder) {
        return User.builder()
            .name(this.name)
            .email(this.email)
            .phone(this.phone)
            .password(encoder.encode(this.password))
            .company(company)
            .build();
    }

}