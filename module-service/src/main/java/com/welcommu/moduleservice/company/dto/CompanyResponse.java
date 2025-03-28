package com.welcommu.moduleservice.company.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class CompanyResponse {
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
}