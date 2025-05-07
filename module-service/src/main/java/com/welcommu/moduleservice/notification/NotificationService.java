package com.welcommu.moduleservice.notification;

import com.welcommu.moduledomain.notification.Notification;
import com.welcommu.moduleinfra.notification.NotificationRepository;
import com.welcommu.moduleservice.notification.dto.NotificationRequest;
import com.welcommu.moduleservice.notification.dto.NotificationResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = new SseEmitter(1000L * 60L * 60L);//1시간
        emitters.put(userId, emitter);

        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));

        try {
            emitter.send(SseEmitter.event().name("connect").data("connected"));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }

    public List<NotificationResponse> getNotifications(Long userId) {
        List<Notification> notifications = notificationRepository.findByReceiverIdOrderByCreatedAtDesc(
            userId);
        return notifications.stream()
            .map(NotificationResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new NoSuchElementException("Notification not found"));
        notification.markAsRead();
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        notificationRepository.markAllAsReadByUserId(userId);
    }

    @Transactional
    public void sendNotification(NotificationRequest request) {
        Notification notification = request.toEntity(request);
        Notification savedNotification = notificationRepository.save(notification);
        NotificationResponse notificationResponse = NotificationResponse.from(savedNotification);

        //SSE 구독중인 경우만 즉시 Push
        SseEmitter emitter = emitters.get(request.getReceiverId());
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                    .name("notification")
                    .data(notificationResponse));
            } catch (IOException | IllegalStateException e) {
                emitter.completeWithError(e); // optional
                emitters.remove(request.getReceiverId());
            }
        }
    }

    @Transactional
    public void deleteNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new NoSuchElementException("Notification not found"));
        notification.setDeletedAt();
    }
}