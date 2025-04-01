package com.welcommu.moduledomain.company.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CompanyResponse {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String businessNumber;
}
