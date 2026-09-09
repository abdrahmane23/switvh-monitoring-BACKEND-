package com.example.project.Services;


import com.example.project.Domain.Entities.Interface;
import com.example.project.Domain.Entities.Vlan;

import java.util.List;
import java.util.UUID;

public interface VlanService {
    List<Vlan> getAllVlans(UUID switchId);
}
