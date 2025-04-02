package com.welcommu.moduledomain.company.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyRequest {
    private String name;
    private String address;
    private String phone;
    private String email;
    private String businessNumber;
}