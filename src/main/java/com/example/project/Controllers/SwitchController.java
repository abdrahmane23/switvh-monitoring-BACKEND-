package com.example.project.Controllers;

import com.example.project.Domain.Dtos.CreateSwitchRequestDto;
import com.example.project.Domain.Dtos.GetSwitchResponseDto;
import com.example.project.Domain.Entities.Switch;
import com.example.project.Mapper.SwitchMapper;
import com.example.project.Services.SwitchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/switch")
@RequiredArgsConstructor
public class SwitchController {
    private final SwitchService switchService;
    private final SwitchMapper switchMapper;


    @PostMapping()
    public ResponseEntity<Void> createSwitch(
            @Valid @RequestBody CreateSwitchRequestDto requestDto
    ) {
        switchService.createSwitch(requestDto);
        return ResponseEntity.ok().build();
    }
    @PutMapping("{switchId}")
    public ResponseEntity<Void> editSwitch (
            @PathVariable UUID switchId,
            @RequestBody @Valid CreateSwitchRequestDto editSwitchRequestDto
    ) {
        switchService.editSwitch(editSwitchRequestDto,switchId);
        return ResponseEntity.ok().build();
    }
    @GetMapping()
    public ResponseEntity<List<GetSwitchResponseDto>> getAllSwitches() {
        List<Switch> switches = switchService.getAllSwitches();
        return ResponseEntity.ok(switches.stream().map(switchMapper::toDto).toList());
    }
    @DeleteMapping("/{switchId}")
    public ResponseEntity<Void> deleteSwitch(
            @PathVariable UUID switchId
    ) {
        switchService.deleteSwitch(switchId);
        return ResponseEntity.ok().build();
    }

}
