package com.example.project.Controllers;

import com.example.project.Domain.Dtos.CreateServiceRequestDto;
import com.example.project.Domain.Dtos.CreateSwitchRequestDto;
import com.example.project.Domain.Dtos.GetServiceDetailsResponseDto;
import com.example.project.Domain.Dtos.GetServiceResponseDto;
import com.example.project.Domain.Entities.Service;
import com.example.project.Mapper.ServiceMapper;
import com.example.project.Services.ServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/service")
@RequiredArgsConstructor
public class ServiceController {
    private final ServiceService serviceService;
    private final ServiceMapper serviceMapper;
    @PostMapping
    public ResponseEntity<Void> createService(
            @Valid @RequestBody CreateServiceRequestDto createServiceRequestDto
            ) {
        serviceService.createService(createServiceRequestDto);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/{serviceId}")
    public ResponseEntity<Void> editService(
            @Valid @RequestBody CreateServiceRequestDto createServiceRequestDto,
            @PathVariable UUID serviceId
    ) {
        serviceService.editService(createServiceRequestDto, serviceId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<GetServiceResponseDto>> getAllServices() {
        List<Service> services = serviceService.getAllServices();

        return ResponseEntity.ok(services.stream().map(serviceMapper::toDto).toList());
    }
    @DeleteMapping("/{serviceId}")
    public ResponseEntity<Void> deleteService(
            @PathVariable UUID serviceId
    ) {
        serviceService.deleteService(serviceId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/{serviceId}")
    public ResponseEntity<GetServiceDetailsResponseDto> getServiceDetails (
            @PathVariable UUID serviceId
    ) {
        Service serviceDetails = serviceService.getServiceDetails(serviceId);

        return ResponseEntity.ok(serviceMapper.toDetailsDto(serviceDetails));
    }

}
