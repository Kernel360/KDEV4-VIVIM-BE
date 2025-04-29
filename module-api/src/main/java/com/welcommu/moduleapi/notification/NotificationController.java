package com.welcommu.moduleapi.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    // Subscribe for SSE
    @GetMapping("/subscribe/{userId}")
    public SseEmitter subscribe(@PathVariable Long userId) {
        return service.subscribe(userId);
    }

    // Fetch notifications
    @GetMapping
    public List<Notification> getNotifications(
        @RequestParam Long userId,
        @RequestParam(defaultValue = "false") boolean unreadOnly
    ) {
        return service.getNotifications(userId, unreadOnly);
    }

    // Mark as read
    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        Optional<Notification> opt = ((NotificationRepository) ((NotificationServiceImpl) service).repository).findById(
            id);
        if (opt.isPresent()) {
            Notification n = opt.get();
            n.setRead(true);
            ((NotificationServiceImpl) service).repository.save(n);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
