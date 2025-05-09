package com.welcommu.moduleservice.notification.dto;


import com.welcommu.moduledomain.notification.Notification;
import com.welcommu.moduledomain.notification.NotificationType;
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
    private String content;
    private NotificationType type;
    private Long typeId;
    private LocalDateTime createdAt;
    private boolean read;

    public static NotificationResponse from(Notification notification) {
        return NotificationResponse.builder()
            .id(notification.getId())
            .content(notification.getContent())
            .type(notification.getNotificationType())
            .typeId(notification.getTypeId())
            .createdAt(notification.getCreatedAt())
            .read(notification.isRead())
            .build();
    }
}
