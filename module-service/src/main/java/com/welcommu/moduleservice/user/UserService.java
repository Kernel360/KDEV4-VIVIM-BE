package com.welcommu.moduleservice.user;

import com.welcommu.moduledomain.company.Company;
import com.welcommu.moduledomain.user.User;
import com.welcommu.modulerepository.company.CompanyRepository;
import com.welcommu.modulerepository.user.UserRepository;
import com.welcommu.moduleservice.user.dto.UserRequest;
import com.welcommu.moduleservice.user.dto.UserResponse;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyRepository companyRepository;

    @Transactional
    public UserResponse createUser(UserRequest userRequest) {

        Company company = companyRepository.findById(userRequest.getCompanyId())
            .orElseThrow(() -> new IllegalArgumentException("Company not found with id " + userRequest.getCompanyId()));

        String encryptedPassword = passwordEncoder.encode(userRequest.getPassword());

        User user = User.builder()
            .name(userRequest.getName())
            .email(userRequest.getEmail())
            .phone(userRequest.getPhone())
            .password(userRequest.getPassword())
            .company(company)
       .build();

        User savedUser = userRepository.saveAndFlush(user);

        return UserResponse.from(savedUser);
    }

    public List<UserResponse> getAllUsers() {

        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserResponse::from)
                .collect(Collectors.toList());
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    public User updateUser(Long id, UserRequest updatedUserRequest) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            log.info("기존 사용자 데이터: " + user);
            user.setName(updatedUserRequest.getName());
            user.setEmail(updatedUserRequest.getEmail());
            user.setPhone(updatedUserRequest.getPhone());
            user.setModifiedAt(updatedUserRequest.getModifiedAt());

            return userRepository.save(user);
        } else {
            log.info("사용자 존재하지 않음: id=" + id);
            throw new RuntimeException("User not found with id " + id);
        }
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // 사용자 비활성화 (soft delete)
    public void softDeleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User existingUser = user.get();
            existingUser.setIsDeleted(true);
            existingUser.setDeletedAt(java.time.LocalDateTime.now());
            userRepository.save(existingUser);
        } else {
            throw new RuntimeException("User not found with id " + id);
        }
    }
}
