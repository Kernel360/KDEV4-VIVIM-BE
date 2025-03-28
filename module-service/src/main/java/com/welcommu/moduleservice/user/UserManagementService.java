package com.welcommu.moduleservice.user;

import com.welcommu.moduledomain.company.Company;
import com.welcommu.moduledomain.user.User;
import com.welcommu.moduleservice.user.dto.UserRequest;
import com.welcommu.moduleservice.user.dto.UserResponse;
import com.welcommu.modulerepository.company.CompanyRepository;
import com.welcommu.modulerepository.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserManagementService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyRepository companyRepository;

    @Autowired
    public UserManagementService(UserRepository userRepository, PasswordEncoder passwordEncoder, CompanyRepository companyRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.companyRepository = companyRepository;
    }

    // 사용자 등록
    @Transactional
    public UserResponse createUser(UserRequest userRequest) {
        // UserRequest로부터 User 엔티티 생성

        User user = User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .build();

        // 비밀번호 암호화
        String encryptedPassword = passwordEncoder.encode(userRequest.getPassword());

        // 암호화된 비밀번호를 User 객체에 설정
        user.setPassword(encryptedPassword);

        // companyId가 전달되었다면, 해당 company를 조회하여 설정
        Company company = companyRepository.findById(userRequest.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("Company not found with id " + userRequest.getCompanyId()));

        user.setCompany(company);

        // 사용자 저장
        User savedUser = userRepository.saveAndFlush(user);

        // UserResponse 생성 후 반환
        return UserResponse.from(savedUser);

    }


    // 사용자 전체 목록 조회
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ID로 사용자 조회
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // 이메일로 사용자 조회
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);  // Optional<User> 자체 반환
    }

    // 전화번호로 사용자 조회
    public Optional<User> getUserByPhone(String phone) {
        return userRepository.findByPhone(phone);  // Optional<User> 자체 반환
    }

    // 사용자 정보 수정
    public User updateUser(Long id, UserRequest updatedUserRequest) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            System.out.println("기존 사용자 데이터: " + user);  // 기존 데이터 확인

            // 기존 객체를 수정
            user.setName(updatedUserRequest.getName());  // 수정된 필드
            user.setEmail(updatedUserRequest.getEmail());
            user.setPhone(updatedUserRequest.getPhone());
            user.setModifiedAt(updatedUserRequest.getModifiedAt());  // 수정된 날짜

            // 수정된 객체 저장
            return userRepository.save(user);
        } else {
            System.out.println("사용자 존재하지 않음: id=" + id);  // 잘못된 id 확인 가능
            throw new RuntimeException("User not found with id " + id);
        }
    }

    // 사용자 삭제
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
