package com.example.project.Controllers;


import com.example.project.Domain.Dtos.GetVlanResponseDto;
import com.example.project.Domain.Entities.Vlan;
import com.example.project.Mapper.VlanMapper;
import com.example.project.Services.VlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vlan")
@RequiredArgsConstructor
public class VlanController {
    private final VlanService vlanService;
    private final VlanMapper vlanMapper;

    @GetMapping("{switchId}")
    public ResponseEntity<List<GetVlanResponseDto>> getAllVlans(
            @PathVariable UUID switchId
    ) {
        List<Vlan> vlans = vlanService.getAllVlans(switchId);
        return ResponseEntity.ok(vlans.stream().map(vlanMapper::toDto).toList());
    }
}
