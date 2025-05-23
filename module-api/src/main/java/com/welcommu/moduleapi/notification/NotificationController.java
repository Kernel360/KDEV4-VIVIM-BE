package com.welcommu.moduleapi.notification;

import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduledomain.auth.AuthUserDetailsImpl;
import com.welcommu.moduleservice.notification.NotificationService;
import com.welcommu.moduleservice.notification.dto.NotificationRequest;
import com.welcommu.moduleservice.notification.dto.NotificationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "알림 API", description = "알림을 생성, 전송, 조회, 읽음, 삭제시킬 수 있으며 SSE연결을 통한 실시간 알림도 가능합니다.")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    @Operation(summary = "알림을 전송합니다.")
    public ResponseEntity<ApiResponse> sendNotification(@RequestBody NotificationRequest request) {
        notificationService.sendNotification(request);
        return ResponseEntity.ok()
            .body(new ApiResponse(HttpStatus.CREATED.value(), "알림이 등록되었습니다."));
    }

    @GetMapping("/subscribe")
    @Operation(summary = "SSE에 연결합니다.")
    public ResponseEntity<SseEmitter> subscribe(
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails) throws IOException {
        SseEmitter emitter = notificationService.subscribe(userDetails.getUser().getId());
        return ResponseEntity.ok().body(emitter);
    }

    @GetMapping
    @Operation(summary = "알림을 조회 합니다.")
    public ResponseEntity<List<NotificationResponse>> getNotifications(
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails
    ) {
        return ResponseEntity.ok()
            .body(notificationService.getNotifications(userDetails.getUser().getId()));
    }

    @PatchMapping("/{notificationId}/read")
    @Operation(summary = "알림을 읽음 처리 합니다.")
    public ResponseEntity<ApiResponse> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "알림을 읽었습니다."));
    }

    @PatchMapping("/read-all")
    @Operation(summary = "알림을 모두 읽습니다.")
    public ResponseEntity<Void> markAllNotificationsAsRead(
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails) {
        notificationService.markAllAsRead(userDetails.getUser().getId());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{notificationId}")
    @Operation(summary = "알림을 삭제합니다.")
    public ResponseEntity<ApiResponse> deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "알림이 삭제되었습니다."));
    }
}
