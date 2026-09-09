package com.example.project.Mapper;


import com.example.project.Domain.Dtos.NotificationDto;
import com.example.project.Domain.Entities.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface NotificationMapper {
    NotificationDto toDto(Notification notification);
}
