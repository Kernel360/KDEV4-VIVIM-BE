package com.welcommu.moduleservice.notification;

import com.welcommu.moduleservice.notification.dto.NotificationRequest;
import com.welcommu.moduleservice.notification.dto.NotificationResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {

    SseEmitter subscribe(Long userId) throws IOException;

    List<NotificationResponse> getNotifications(Long userId);

    void markAsRead(Long notificationId);

    void markAllAsRead(Long userId);

    void sendNotification(NotificationRequest request);

    void deleteNotification(Long notificationId);
}