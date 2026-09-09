package com.example.project.Mapper;


import com.example.project.Domain.Dtos.GetVlanResponseDto;
import com.example.project.Domain.Entities.Vlan;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VlanMapper {

    GetVlanResponseDto toDto(Vlan vlan);
}
