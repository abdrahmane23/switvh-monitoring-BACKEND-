package com.example.project.Services;

import com.example.project.Domain.Dtos.CreateServiceRequestDto;
import com.example.project.Domain.Dtos.GetServiceResponseDto;
import com.example.project.Domain.Entities.Service;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface ServiceService {

    void createService(CreateServiceRequestDto createServiceRequestDto);

    void editService(CreateServiceRequestDto createServiceRequestDto, UUID serviceId);

    List<Service> getAllServices();

    void deleteService(UUID serviceId);

    Service getServiceDetails(UUID serviceId);
}
