package com.welcommu.moduleservice.notification.dto;


import com.welcommu.moduledomain.notification.Notification;
import com.welcommu.moduledomain.notification.NotificationStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

    private Long id;
    private String title;
    private String content;
    private NotificationStatus status;
    private LocalDateTime createdAt;
    private boolean read;

    public static NotificationResponse from(Notification notification) {
        return NotificationResponse.builder()
            .id(notification.getId())
            .title(notification.getTitle())
            .content(notification.getContent())
            .status(notification.getNotificationStatus())
            .createdAt(notification.getCreatedAt())
            .read(notification.isRead())
            .build();
    }
}
