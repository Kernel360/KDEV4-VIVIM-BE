package com.welcommu.moduledomain.user;

import com.welcommu.moduledomain.company.Company;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name="users")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = true)
    private String name;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 20, nullable = true)
    private String phone;

    private LocalDateTime passwordModifiedAt;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;
    private Boolean isDeleted;


    // Company와의 연관 관계를 나타내는 필드
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = true)
    private Company company;
}
