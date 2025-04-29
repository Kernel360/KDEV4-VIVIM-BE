package com.welcommu.moduleservice.user;

import com.welcommu.moduledomain.company.Company;
import com.welcommu.moduledomain.company.CompanyRole;
import com.welcommu.moduledomain.user.User;
import com.welcommu.moduleservice.user.dto.UserModifyRequest;
import com.welcommu.moduleservice.user.dto.UserRequest;
import com.welcommu.moduleservice.user.dto.UserResponse;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    void createUser(UserRequest request, Long creatorId);
    UserResponse modifyUser(Long id, Long creatorId, UserModifyRequest request);
    Company findCompany(Long request);
    Page<UserResponse> searchUsers(String name, String email, String phone,
        Long companyId, CompanyRole companyRole,
        Boolean isDeleted, Pageable pageable);
    boolean resetPasswordWithoutLogin(String email);
    boolean modifyPassword(Long id, String password);
    void deleteUser(Long id,Long actorId );
    void softDeleteUser(Long id);
    List<UserResponse> getAllUsers();
    Optional<User> getUserById(Long id);
    Optional<User> getUserByEmail(String email);
    Optional<User> getUserByPhone(String phone);

}
