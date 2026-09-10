package com.example.project.Services.Impls;


import com.example.project.Domain.Dtos.NotificationDto;
import com.example.project.Domain.Entities.Notification;
import com.example.project.Repositories.NotificationRepo;
import com.example.project.Services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepo notificationRepo;

    private final List<SseEmitter> usersList = new CopyOnWriteArrayList<>();

    @Override
    public SseEmitter SubscribeToNotifications() {
        SseEmitter emitter = new SseEmitter(60 * 60 * 1000L); // Infinite timeout
        usersList.add(emitter);
        emitter.onCompletion(() -> usersList.remove(emitter));
        emitter.onTimeout(() -> usersList.remove(emitter));

        return emitter;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendNotificationToAll(NotificationDto notificationDto) {
        Notification notification= new Notification();
        notification.setMessage(notificationDto.getMessage());
        notificationRepo.save(notification);

        usersList.forEach(
                emitter -> {
                    try {
                        emitter.send(SseEmitter.event().name("notification").data(notificationDto));

                    } catch (Exception e) {
                        usersList.remove(emitter);
                    }
                }
        );
    }

    @Override
    public List<Notification> fetchAllNotification() {
        return notificationRepo.findTop10ByOrderByCreatedAtDesc();

    }

    @Override
    @Transactional
    public void deleteExpiredNotifications() {
        notificationRepo.deleteByCreatedAtBefore(LocalDateTime.now().minusMinutes(10));
    }
}
