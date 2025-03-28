package com.welcommu.moduledomain.company.dto;

public class CompanyResponse {
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;

    public CompanyResponse(Long id, String name, String address, String phoneNumber, String email) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}