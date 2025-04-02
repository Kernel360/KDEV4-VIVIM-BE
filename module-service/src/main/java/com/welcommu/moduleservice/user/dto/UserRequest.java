package com.welcommu.moduleservice.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserRequest {

    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Size(min = 4)
    private String password;

    private String name;

    private String phone;
  
    @NotNull
    private Long companyId;

    private LocalDateTime modifiedAt;
}