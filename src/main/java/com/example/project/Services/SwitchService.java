package com.example.project.Services;


import com.example.project.Domain.Dtos.CreateSwitchRequestDto;
import com.example.project.Domain.Entities.Switch;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface SwitchService {

    void createSwitch(CreateSwitchRequestDto newSwitch);

    List<Switch> getAllSwitches();
    void makeSwitchDown(Switch switchToDown);

    void deleteSwitch(UUID switchId);

    void editSwitch( CreateSwitchRequestDto editSwitchRequestDto,UUID switchId);
}
