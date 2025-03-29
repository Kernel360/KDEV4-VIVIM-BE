package com.welcommu.moduledomain.company.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyResponse {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String bussinessNumber;

    public CompanyResponse(Long id, String name, String address, String phone, String email, String bussinessNumber) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.bussinessNumber = bussinessNumber;
    }
}