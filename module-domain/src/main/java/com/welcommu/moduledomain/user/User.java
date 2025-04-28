package com.welcommu.moduledomain.user;

import com.welcommu.moduledomain.company.Company;
import com.welcommu.moduledomain.company.CompanyRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 20, nullable = false)
    private String phone;

    private LocalDateTime passwordModifiedAt;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;
    private Boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    public CompanyRole getRole() {
        return this.company != null ? this.company.getCompanyRole() : null;
    }
}
