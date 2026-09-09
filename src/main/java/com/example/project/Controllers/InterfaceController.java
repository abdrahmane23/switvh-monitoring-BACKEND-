package com.example.project.Controllers;


import com.example.project.Domain.Dtos.ConnectionHistoryForInterfaceResponseDto;
import com.example.project.Domain.Dtos.GetInterfaceDetailsResponseDto;
import com.example.project.Domain.Dtos.GetInterfaceResponseDto;
import com.example.project.Domain.Entities.Connection;
import com.example.project.Domain.Entities.Interface;
import com.example.project.Mapper.ConnectionMapper;
import com.example.project.Mapper.InterfaceMapper;
import com.example.project.Services.InterfaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/interface")
@RequiredArgsConstructor
public class InterfaceController {

    private final InterfaceService interfaceService;
    private final InterfaceMapper interfaceMapper;
    private final ConnectionMapper connectionMapper;
    @GetMapping("vlan/{vlanId}")
    public ResponseEntity<List<GetInterfaceResponseDto>>  getInterfacesByVlan(
            @PathVariable UUID vlanId
    ){
        List<Interface> interfaces = interfaceService.getInterfaces(vlanId);
        return ResponseEntity.ok(interfaces.stream().map(interfaceMapper::toVlanInterfaceDto).toList());
    }
    @GetMapping("{interfaceId}")
    public ResponseEntity<GetInterfaceDetailsResponseDto> getInterfaces(
            @PathVariable UUID interfaceId
    ){
        Interface interfaceDetails = interfaceService.getInterfaceDetails(interfaceId);
        return ResponseEntity.ok(interfaceMapper.toDto(interfaceDetails));
    }
}
