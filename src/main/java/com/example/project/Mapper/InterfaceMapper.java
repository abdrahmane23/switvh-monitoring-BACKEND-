package com.example.project.Mapper;


import com.example.project.Domain.Dtos.*;
import com.example.project.Domain.Entities.Alert;
import com.example.project.Domain.Entities.Connection;
import com.example.project.Domain.Entities.Interface;
import com.example.project.Domain.Entities.Ordinateur;
import com.example.project.Domain.Enums.CONNECTION_STATUS_ENUM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface InterfaceMapper {
    @Mapping(target = "connection", source = "connections")
    @Mapping(target = "alert", source = "alerts")
    GetInterfaceDetailsResponseDto toDto(Interface Interface);
    default ActiveConnectionForInterfaceResponseDto toActiveConnectionDto(List<Connection> connections){
        Optional<Connection> con =connections
                .stream()
                .filter(c-> c.getStatus()== CONNECTION_STATUS_ENUM.ACTIVE)
                .findFirst();

        if(con.isPresent()){
            Connection connection = con.get();
            ActiveConnectionForInterfaceResponseDto dto = new ActiveConnectionForInterfaceResponseDto();
            dto.setId(connection.getId());
            dto.setStatus(connection.getStatus());
            dto.setOrdinateur(toOrdinateurDto(connection.getOrdinateur()));
            return dto;
        }

        return null;
    }
    default ActiveAlert  toActiveAlertDto(List<Alert> alerts){
        Optional<Alert> alert= alerts
                .stream()
                .filter(a-> a.getCreatedAt().isAfter(LocalDateTime.now().minusSeconds(30)))
                .findFirst();
        if(alert.isPresent()){
            ActiveAlert activeAlert = new ActiveAlert();
            activeAlert.setId(alert.get().getId());
            activeAlert.setMacAddress(alert.get().getMacAddress());
            activeAlert.setCreatedAt(alert.get().getCreatedAt());
            return activeAlert;
        }
        return null;
    }
    GetOrdinateurResponseDto toOrdinateurDto(Ordinateur ordinateur);
    GetInterfaceResponseDto toVlanInterfaceDto(Interface Interface);
}
