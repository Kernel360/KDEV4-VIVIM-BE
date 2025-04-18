package com.welcommu.modulerepository.user;

import com.welcommu.moduledomain.company.Company;
import com.welcommu.moduledomain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    List<User> findByCompanyId(Long companyId);  // 회사 ID로 직원 목록 조회

    @Query("SELECT u FROM User u JOIN FETCH u.company WHERE u.company = :company")
    List<User> findByCompanyWithJoinFetch(@Param("company") Company company);
}
