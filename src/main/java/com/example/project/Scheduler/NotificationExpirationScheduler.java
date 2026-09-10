package com.example.project.Scheduler;


import com.example.project.Services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationExpirationScheduler {
    private final NotificationService notificationService;
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void deleteExpiredNotifications(){
        notificationService.deleteExpiredNotifications();//delete outdated notification especially that synchronization happens every 30seconds

    }
}
