package com.example.project.Mapper;


import com.example.project.Domain.Dtos.ConnectionHistoryForInterfaceResponseDto;
import com.example.project.Domain.Dtos.ConnectionHistoryForPcResponseDto;
import com.example.project.Domain.Dtos.GetInterfaceResponseDto;
import com.example.project.Domain.Dtos.GetOrdinateurResponseDto;
import com.example.project.Domain.Entities.Connection;
import com.example.project.Domain.Entities.Interface;
import com.example.project.Domain.Entities.Ordinateur;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ConnectionMapper {
    ConnectionHistoryForInterfaceResponseDto toConnectionHistoryResponseDto(Connection connection);
    GetOrdinateurResponseDto toOrdinateurDto(Ordinateur ordinateur);
    ConnectionHistoryForPcResponseDto toConnectionHistoryForPcResponseDto(Connection connection);
    GetInterfaceResponseDto toInterfaceDto(Interface switchInterface);
}
