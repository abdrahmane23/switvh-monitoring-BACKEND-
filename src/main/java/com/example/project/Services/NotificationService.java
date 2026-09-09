package com.example.project.Services;

import com.example.project.Domain.Dtos.NotificationDto;
import com.example.project.Domain.Entities.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.UUID;

public interface NotificationService {

    SseEmitter SubscribeToNotifications();
    void sendNotificationToAll(NotificationDto message);

    List<Notification> fetchAllNotification();
}
