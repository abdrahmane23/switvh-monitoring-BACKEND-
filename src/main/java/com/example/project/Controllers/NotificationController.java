package com.example.project.Controllers;



import com.example.project.Domain.Dtos.NotificationDto;
import com.example.project.Domain.Entities.Notification;
import com.example.project.Mapper.NotificationMapper;
import com.example.project.Services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("api/notifications")
@RequiredArgsConstructor

public class NotificationController {
    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;

    @GetMapping("/subscribe")
    public SseEmitter subscribeToNotifications() {
        SseEmitter emitter= notificationService.SubscribeToNotifications();
        return emitter;
    }
    @GetMapping()
    public ResponseEntity<List<NotificationDto>> fetchAllNotifications(){
        List<Notification> notifications = notificationService.fetchAllNotification();
        return ResponseEntity.ok(notifications.stream().map(notificationMapper::toDto).toList());

    }
}
