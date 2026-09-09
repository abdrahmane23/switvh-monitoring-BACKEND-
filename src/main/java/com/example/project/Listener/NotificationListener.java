package com.example.project.Listener;


import com.example.project.Domain.Dtos.NotificationDto;
import com.example.project.Event.InterfaceDownEvent;
import com.example.project.Event.SwitchDownEvent;
import com.example.project.Event.UnknownMacAddressEvent;
import com.example.project.Services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class NotificationListener {
    private final NotificationService notificationService;

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handleInterfaceDown(InterfaceDownEvent event) {
        NotificationDto message = new NotificationDto(
                "L'interface " + event.interfaceName() + " est à DOWN",
                LocalDateTime.now()
        );

        notificationService.sendNotificationToAll(message);
    }
    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handleSwitchDown(SwitchDownEvent event) {
        NotificationDto message = new NotificationDto(
                "Switch: " + event.switchName() + " est à hors-ligne",
                LocalDateTime.now()
        );

        notificationService.sendNotificationToAll(message);
    }
    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handleUnknownMacAddress(UnknownMacAddressEvent event) {
        NotificationDto message = new NotificationDto(
                "Adresse MAC inconnue détectée: " + event.macAdress()+"au interface: "+ event.InterfaceName(),
                LocalDateTime.now()
        );

        notificationService.sendNotificationToAll(message);
    }

}
