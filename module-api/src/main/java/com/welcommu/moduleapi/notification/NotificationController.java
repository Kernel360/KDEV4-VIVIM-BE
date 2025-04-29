package com.welcommu.moduleapi.notification;

import com.welcommu.modulecommon.dto.ApiResponse;
import com.welcommu.moduledomain.auth.AuthUserDetailsImpl;
import com.welcommu.moduledomain.notification.Notification;
import com.welcommu.moduleservice.notification.NotificationService;
import com.welcommu.moduleservice.notification.dto.NotificationRequest;
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

    @GetMapping("/subscribe/{userId}")
    public ResponseEntity<SseEmitter> subscribe(@PathVariable Long userId) {
        SseEmitter emitter = notificationService.subscribe(userId);
        return ResponseEntity.ok().body(emitter);
    }

    @GetMapping("/{userId}")
    public List<Notification> getNotifications(
        @PathVariable Long userId
    ) {
        return notificationService.getNotifications(userId);
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}
