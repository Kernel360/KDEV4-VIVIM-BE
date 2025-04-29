package com.welcommu.moduleservice.notification;

import com.welcommu.moduledomain.notification.Notification;
import com.welcommu.moduleinfra.notification.NotificationRepository;
import com.welcommu.moduleservice.notification.dto.NotificationRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
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

    public void disconnect(Long userId) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            emitter.complete(); // 연결 정상 종료
            emitters.remove(userId); // 맵에서도 제거
        }
    }

    public List<Notification> getNotifications(Long userId) {
        return notificationRepository.findByReceiverIdAndIsReadFalseOrderByCreatedAtDesc(
            userId);
    }

    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new NoSuchElementException("Notification not found"));
        notification.markAsRead();
    }

    @Transactional
    public void sendNotification(Long receiverId, NotificationRequest request) throws IOException {
        Notification notification = request.toEntity(request);
        Notification savedNotification = notificationRepository.save(notification);

        //SSE 구독중인 경우만 즉시 Push
        SseEmitter emitter = emitters.get(receiverId);
        if (emitter != null) {
            savedNotification.markAsRead();
            emitter.send(SseEmitter.event()
                .name("notification")
                .data(savedNotification));
        }
    }

    @Transactional
    public void deleteNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new NoSuchElementException("Notification not found"));
        notification.setDeletedAt();
    }
}