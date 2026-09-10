package com.example.project.Scheduler;


import com.example.project.Domain.Dtos.NotificationDto;
import com.example.project.Domain.Entities.Switch;
import com.example.project.Exceptions.SwitchConnectionException;
import com.example.project.Repositories.SwitchRepo;
import com.example.project.Services.MonitoringService;
import com.example.project.Services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static org.antlr.v4.runtime.tree.xpath.XPath.findAll;

@Component
@RequiredArgsConstructor
public class SwitchMonitorer {


    private final MonitoringService monitoringService;
    private final SwitchRepo switchRepo;

    @Scheduled(fixedRate = 30_000)
    public void monitorSwitches() {
        System.out.println("Monitoring switches...");
        List<Switch> switches = switchRepo.findAll();
        for (Switch sw : switches) {
            try {
                monitoringService.monitorSwitch(sw);
            } catch (SwitchConnectionException ex) {
                System.out.println(ex);
            }
        }
    }
}
