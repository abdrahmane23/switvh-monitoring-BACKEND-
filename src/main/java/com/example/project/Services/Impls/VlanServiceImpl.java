package com.example.project.Services.Impls;

import com.example.project.Domain.Entities.Switch;
import com.example.project.Domain.Entities.Vlan;
import com.example.project.Exceptions.VlanNotFoundException;
import com.example.project.Repositories.SwitchRepo;
import com.example.project.Services.VlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VlanServiceImpl implements VlanService {
    private final SwitchRepo switchRepo;
    @Override
    public List<Vlan> getAllVlans(UUID switchId) {
        Switch switchEntity= switchRepo.findById(switchId).orElseThrow(
                () -> new VlanNotFoundException("Switch not found")
        );
        return switchEntity.getVlans();

    }
}
