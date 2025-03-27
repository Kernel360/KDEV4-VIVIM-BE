package com.welcommu.moduleapi.user;

import com.welcommu.moduledomain.user.User;
import com.welcommu.moduledomain.user.dto.UserRequest;
import com.welcommu.moduledomain.user.dto.UserResponse;
import com.welcommu.moduleservice.user.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserManagementService userManagementService;

    @Autowired
    public UserController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    // 사용자 등록
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) {
        System.out.println("Received user: " + userRequest); // 요청 받은 데이터 출력

        // 서비스에서 사용자 등록 처리 후 응답 받기
        UserResponse createdUserResponse = userManagementService.createUser(userRequest);

        // 생성된 사용자 정보와 함께 HTTP 201 응답 반환
        return new ResponseEntity<>(createdUserResponse, HttpStatus.CREATED);
    public ResponseEntity<User> createUser(@RequestBody User user) {
        System.out.println("Received user: " + user); // 요청 받은 데이터 출력
        User createdUser = userManagementService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    // 전체 사용자 조회
    @GetMapping
    public List<User> getAllUsers() {
        return userManagementService.getAllUsers();
    }

    // ID로 사용자 조회
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userManagementService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 이메일로 사용자 조회
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userManagementService.getUserByEmail(email);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 전화번호로 사용자 조회
    @GetMapping("/phone/{phone}")
    public ResponseEntity<User> getUserByPhone(@PathVariable String phone) {
        Optional<User> user = userManagementService.getUserByPhone(phone);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 사용자 수정
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        try {
            User user = userManagementService.updateUser(id, updatedUser);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 사용자 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userManagementService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // 사용자 비활성화 (soft delete)
    @DeleteMapping("/soft/{id}")
    public ResponseEntity<Void> softDeleteUser(@PathVariable Long id) {
        userManagementService.softDeleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
