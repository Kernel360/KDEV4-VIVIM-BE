package com.welcommu.moduleapi.user;

import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduledomain.auth.AuthUserDetailsImpl;
import com.welcommu.moduledomain.user.User;
import com.welcommu.moduleservice.user.dto.UserRequest;
import com.welcommu.moduleservice.user.dto.UserResponse;
import com.welcommu.moduleservice.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "유저 API", description = "유저를 생성, 수정, 삭제시킬 수 있습니다.")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "유저를 생성합니다")
    public ResponseEntity<ApiResponse> createUser(@RequestBody UserRequest userRequest, @AuthenticationPrincipal AuthUserDetailsImpl userDetails) {
        log.info("Received user: {}", userRequest);
        Long actorId = userDetails.getUser().getId();
        userService.createUser(userRequest, actorId);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "사용자 생성에 성공했습니다."));
    }

    @GetMapping
    @Operation(summary = "모든 유저를 조회합니다.")
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "id를 바탕으로 유저를 개별 조회합니다.")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            UserResponse response = UserResponse.from(user.get());
            return ResponseEntity.ok(new ApiResponse(200, "User found", response));
        } else {
            ApiResponse apiResponse = new ApiResponse(404, "User not found with id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
        }
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "email를 바탕으로 유저를 개별 조회합니다.")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.getUserByEmail(email);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/phone/{phone}")
    @Operation(summary = "핸드폰 번호를 바탕으로 유저를 개별 조회합니다.")
    public ResponseEntity<User> getUserByPhone(@PathVariable String phone) {
        Optional<User> user = userService.getUserByPhone(phone);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/resetpassword")
    @Operation(summary = "비밀번호를 잊었을 시 해당 email의 비밀번호를 초기화합니다.")
    public ResponseEntity<ApiResponse> resetPasswordByUserWithoutLogin(@RequestParam String email) {
        if (userService.resetPasswordWithoutLogin(email)) {
            return ResponseEntity.ok(new ApiResponse(200, "Password changed"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(404, "User not found with email: " + email));
        }
    }

    @PutMapping("/modifypassword/{id}")
    @Operation(summary = "id를 바탕으로 비밀번호를 수정합니다.")
    public ResponseEntity<ApiResponse> modifyPasswordByUserWithLogin(@PathVariable Long id,
        @RequestParam String password) {
        if (userService.modifyPassword(id, password)) {
            return ResponseEntity.ok(new ApiResponse(200, "Password changed"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(404, "User not found with id: " + id));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "사용자 정보를 수정합니다.")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,
        @RequestBody UserRequest updatedUserRequest,
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails) {
        try {
            log.info("사용자 수정 요청 받음, id=" + id);
            Long actorId = userDetails.getUser().getId();;
            UserResponse response = userService.modifyUser(id, actorId, updatedUserRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.info("사용자 수정 실패: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "유저를 삭제합니다. (Hard Delete")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id, @AuthenticationPrincipal AuthUserDetailsImpl userDetails) {
        Long actorId = userDetails.getUser().getId();
        userService.deleteUser(id, actorId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/soft/{id}")
    @Operation(summary = "유저를 삭제합니다. (Soft Delete")
    public ResponseEntity<ApiResponse> softDeleteUser(@PathVariable Long id) {
        userService.softDeleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
