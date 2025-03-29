package com.welcommu.moduleapi.user;

import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduledomain.user.User;
import com.welcommu.moduleservice.user.dto.UserRequest;
import com.welcommu.moduleservice.user.dto.UserResponse;
import com.welcommu.moduleservice.user.UserManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
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
    public ResponseEntity<ApiResponse> createUser(@RequestBody UserRequest userRequest) {
        log.info("Received user: {}", userRequest); // 요청 받은 데이터 출력

        // 서비스에서 사용자 등록 처리 후 응답 받기
        UserResponse createdUserResponse = userManagementService.createUser(userRequest);

        // ApiResponse로 상태 코드와 메시지만 포함하여 반환
        ApiResponse apiResponse = new ApiResponse(
                HttpStatus.CREATED.value(),   // 상태 코드 (201)
                "User created successfully"   // 상태 메시지
        );

        // 생성된 사용자 정보와 함께 HTTP 201 응답 반환
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    // 전체 사용자 조회
    @GetMapping
    public List<User> getAllUsers() {
        return userManagementService.getAllUsers();
    }

    // ID로 사용자 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id) {
        Optional<User> user = userManagementService.getUserById(id);
        // 사용자 존재 시 200 OK 응답 반환
        if (user.isPresent()) {
            return ResponseEntity.ok(new ApiResponse(200, "User found", user.get()));
        } else {
            // 사용자 존재하지 않으면 404 응답과 함께 메시지 반환
            ApiResponse apiResponse = new ApiResponse(404, "User not found with id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
        }
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
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserRequest updatedUserRequest) {
        try {
            System.out.println("사용자 수정 요청 받음, id=" + id);
            User user = userManagementService.updateUser(id, updatedUserRequest);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            System.out.println("사용자 수정 실패: " + e.getMessage());
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
