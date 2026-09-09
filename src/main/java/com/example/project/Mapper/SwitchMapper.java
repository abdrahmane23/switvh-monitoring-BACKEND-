package com.example.project.Mapper;


import com.example.project.Domain.Dtos.GetSwitchResponseDto;
import com.example.project.Domain.Entities.Switch;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SwitchMapper {
    GetSwitchResponseDto toDto(Switch switchEntity);

}
