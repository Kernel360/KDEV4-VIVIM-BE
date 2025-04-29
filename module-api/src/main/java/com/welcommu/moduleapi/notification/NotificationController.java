package com.welcommu.moduleapi.notification;

import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduledomain.auth.AuthUserDetailsImpl;
import com.welcommu.moduleservice.notification.NotificationService;
import com.welcommu.moduleservice.notification.dto.NotificationRequest;
import com.welcommu.moduleservice.notification.dto.NotificationResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<ApiResponse> sendNotification(@RequestBody NotificationRequest request,
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails) throws IOException {
        Long receiverId = userDetails.getUser().getId();
        notificationService.sendNotification(receiverId, request);
        return ResponseEntity.ok()
            .body(new ApiResponse(HttpStatus.CREATED.value(), "알림이 등록되었습니다."));
    }

    @GetMapping("/subscribe")
    public ResponseEntity<SseEmitter> subscribe(
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails) {
        SseEmitter emitter = notificationService.subscribe(userDetails.getUser().getId());
        return ResponseEntity.ok().body(emitter);
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotifications(
        @AuthenticationPrincipal AuthUserDetailsImpl userDetails
    ) {
        return ResponseEntity.ok()
            .body(notificationService.getNotifications(userDetails.getUser().getId()));
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponse> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "알림을 읽었습니다."));
    }

    @PatchMapping("/{notificationId}")
    public ResponseEntity<ApiResponse> deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "알림이 삭제되었습니다."));
    }
}
