package com.welcommu.moduledomain.company;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Table(name="companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String businessNumber;
    private String address;
    private String phone;
    private String email;

    @Enumerated(EnumType.STRING)
    private CompanyRole companyRole;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;
    private Boolean isDeleted;

    private String coOwner;
}
