package com.example.project.Services.Impls;


import com.example.project.Domain.Dtos.CreateServiceRequestDto;
import com.example.project.Domain.Entities.Service;
import com.example.project.Exceptions.ServiceAlreadyExistsException;
import com.example.project.Exceptions.ServiceNotFoundException;
import com.example.project.Repositories.ServiceRepo;
import com.example.project.Services.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {
    private final ServiceRepo serviceRepository;


    @Override
    @Transactional
    public void createService(CreateServiceRequestDto createServiceRequestDto) {

        checkServiceUniqueness(createServiceRequestDto.getNom());

        Service service = new Service();
        service.setNom(createServiceRequestDto.getNom());
        service.setEmplacement(createServiceRequestDto.getEmplacement());
        serviceRepository.save(service);
    }

    @Override
    @Transactional
    public void editService(CreateServiceRequestDto createServiceRequestDto, UUID serviceId) {
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found with id: " + serviceId));

        checkServiceUniquenessForEdit(createServiceRequestDto.getNom(),serviceId);
        service.setNom(createServiceRequestDto.getNom());
        service.setEmplacement(createServiceRequestDto.getEmplacement());

    }

    @Override
    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteService(UUID serviceId) {
        if (!serviceRepository.existsById(serviceId)){
            throw new ServiceNotFoundException("Service not found with id: " + serviceId);
        }
            serviceRepository.deleteById(serviceId);
    }

    @Override
    public Service getServiceDetails(UUID serviceId) {
        return serviceRepository.findById(serviceId).orElseThrow(
                ()-> new ServiceNotFoundException("Service not found with id: " + serviceId)
        );
    }

    private void checkServiceUniqueness(String nom) {
        Optional<Service> existingService = serviceRepository.findByNom(nom);
        if (existingService.isPresent()) {
            throw new ServiceAlreadyExistsException("Le service avec le nom " + nom + " existe déjà.");
        }

    }
    private void checkServiceUniquenessForEdit(String nom, UUID serviceId) {
        Optional<Service> existingService = serviceRepository.findByNom(nom);
        if (existingService.isPresent() && !existingService.get().getId().equals(serviceId)) {
            throw new ServiceAlreadyExistsException("Le service avec le nom " + nom + " existe déjà.");
        }

    }

}
