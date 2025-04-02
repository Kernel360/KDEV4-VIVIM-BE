package com.welcommu.moduleapi.user;

import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduledomain.user.User;
import com.welcommu.moduleservice.user.dto.UserRequest;
import com.welcommu.moduleservice.user.dto.UserResponse;
import com.welcommu.moduleservice.user.UserService;
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


    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createUser(@RequestBody UserRequest userRequest) {

        log.info("Received user: {}", userRequest);
        UserResponse createdUserResponse = userService.createUser(userRequest);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "사용자 생성에 성공했습니다."));
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id) {

        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(new ApiResponse(200, "User found", user.get()));
        } else {
            // 사용자 존재하지 않으면 404 응답과 함께 메시지 반환
            ApiResponse apiResponse = new ApiResponse(404, "User not found with id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.getUserByEmail(email);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<User> getUserByPhone(@PathVariable String phone) {
        Optional<User> user = userService.getUserByPhone(phone);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserRequest updatedUserRequest) {
        try {
            log.info("사용자 수정 요청 받음, id=" + id);
            User user = userService.updateUser(id, updatedUserRequest);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            log.info("사용자 수정 실패: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    // 사용자 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // 사용자 비활성화 (soft delete)
    @DeleteMapping("/soft/{id}")
    public ResponseEntity<Void> softDeleteUser(@PathVariable Long id) {
        userService.softDeleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
