package com.welcommu.moduleinfra.user;

import com.welcommu.moduledomain.company.CompanyRole;
import com.welcommu.moduledomain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {
    Page<User> searchByConditions(String name, String email, String phone,
        Long companyId, CompanyRole companyRole,
        Boolean isDeleted, Pageable pageable);
}
