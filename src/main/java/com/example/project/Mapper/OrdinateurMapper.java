package com.example.project.Mapper;

import com.example.project.Domain.Dtos.*;
import com.example.project.Domain.Entities.Connection;
import com.example.project.Domain.Entities.Interface;
import com.example.project.Domain.Entities.Ordinateur;
import com.example.project.Domain.Enums.CONNECTION_STATUS_ENUM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Optional;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface OrdinateurMapper {
    @Mapping(target ="connection",source = "connections")
    GetOrdinateurDetailsResponeDto toDto(Ordinateur ordinateur);
    //we get only active connection
    default ActiveConnectionForPcResponseDto toActiveConnectionDto(List<Connection> connections){
        Optional<Connection> con =connections
                .stream()
                .filter(c-> c.getStatus()== CONNECTION_STATUS_ENUM.ACTIVE)
                .findFirst();

        if(con.isPresent() && con.get().getSwitchInterface()!=null){
            Connection connection = con.get();
            ActiveConnectionForPcResponseDto dto = new ActiveConnectionForPcResponseDto();
            dto.setId(connection.getId());
            dto.setStatus(connection.getStatus());
            dto.setSwitchInterface(toInterfaceDto(connection.getSwitchInterface()));
            return dto;
        }

        return null;
    }
    @Mapping(target = "switchStatus",source = "vlan.switchEntity.status")
    GetInterfaceResponseDto toInterfaceDto(Interface switchIinterface);

}
