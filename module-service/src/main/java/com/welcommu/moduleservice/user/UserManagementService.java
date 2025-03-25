package com.welcommu.moduleservice.user;

import com.welcommu.moduledomain.user.User;
import com.welcommu.modulerepository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserManagementService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserManagementService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 사용자 등록
    public User createUser(User user) {
        // 비밀번호 암호화
        String encryptedPassword = passwordEncoder.encode(user.getPassword());

        // 암호화된 비밀번호를 User 객체에 설정
        user.setPassword(encryptedPassword);

        // 사용자 저장
        return userRepository.save(user);
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
    public User updateUser(Long id, User updatedUser) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setPhone(updatedUser.getPhone());
            user.setModifiedAt(updatedUser.getModifiedAt());
            return userRepository.save(user);
        } else {
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
