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
public class NotificationRequest {

    private Long receiverId;
    private String content;
    private NotificationType type;
    private Long typeId;

    public Notification toEntity(NotificationRequest request) {

        return Notification.builder()
            .receiverId(request.getReceiverId())
            .content(request.getContent())
            .notificationType(request.getType())
            .typeId(request.getTypeId())
            .createdAt(LocalDateTime.now())
            .isRead(false)
            .build();
    }
}
