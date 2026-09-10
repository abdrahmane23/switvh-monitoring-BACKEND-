package com.example.project.Services.Impls;

import com.example.project.Domain.Entities.Connection;
import com.example.project.Domain.Entities.Interface;
import com.example.project.Domain.Entities.Vlan;
import com.example.project.Domain.Enums.CONNECTION_STATUS_ENUM;
import com.example.project.Exceptions.InterfaceNotFoundException;
import com.example.project.Exceptions.VlanNotFoundException;
import com.example.project.Repositories.InterfaceRepo;
import com.example.project.Repositories.VlanRepo;
import com.example.project.Services.InterfaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InterfaceServiceImpl implements InterfaceService {
    private final InterfaceRepo interfaceRepo;
    private final VlanRepo vlanRepo;

    @Override
    public List<Interface> getInterfaces(UUID vlanId) {
        Vlan vlan = vlanRepo.findById(vlanId).orElseThrow(() -> new VlanNotFoundException("vlan n'existe pas"));
        return vlan.getInterfaces();

    }

    @Override
    public Interface getInterfaceDetails(UUID interfaceId) {
        return interfaceRepo.findById(interfaceId).orElseThrow(
                () -> new InterfaceNotFoundException("Interface n'existe pas")
        );

    }

}
