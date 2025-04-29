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
public class NotificationRequest {

    private Long receiverId;
    private String title;
    private String content;
    private NotificationStatus status;

    public Notification toEntity(NotificationRequest request) {

        return Notification.builder()
            .receiverId(request.getReceiverId())
            .title(request.getTitle())
            .content(request.getContent())
            .notificationStatus(request.getStatus())
            .createdAt(LocalDateTime.now())
            .isRead(false)
            .build();
    }
}
