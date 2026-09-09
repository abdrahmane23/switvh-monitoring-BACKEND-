package com.example.project.Mapper;


import com.example.project.Domain.Dtos.GetServiceDetailsResponseDto;
import com.example.project.Domain.Dtos.GetServiceResponseDto;
import com.example.project.Domain.Dtos.OrdinateurResponseDto;
import com.example.project.Domain.Entities.Ordinateur;
import com.example.project.Domain.Entities.Service;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ServiceMapper {
    GetServiceResponseDto toDto (Service service);
    GetServiceDetailsResponseDto toDetailsDto (Service service);

    OrdinateurResponseDto toOrdinateurDto (Ordinateur ordinateur);

}
