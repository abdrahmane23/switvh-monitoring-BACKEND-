package com.example.project.Repositories;


import com.example.project.Domain.Entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, UUID> {
    @Query("SELECT n FROM Notification n")
    List<Notification> findFirst1O();
}
