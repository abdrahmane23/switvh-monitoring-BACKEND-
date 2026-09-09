package com.example.project.Controllers;

import com.example.project.Domain.Dtos.ConnectionHistoryForInterfaceResponseDto;
import com.example.project.Domain.Dtos.ConnectionHistoryForPcResponseDto;
import com.example.project.Domain.Entities.Connection;
import com.example.project.Mapper.ConnectionMapper;
import com.example.project.Services.ConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/connection")
@RequiredArgsConstructor
public class ConnectionController {
    private final ConnectionService connectionService;
    private final ConnectionMapper connectionMapper;


    @GetMapping("/interface/{interfaceId}/history")
    public ResponseEntity<List<ConnectionHistoryForInterfaceResponseDto>> getInterfaceHistory(
            @PathVariable UUID interfaceId
    ){
        List<Connection> interfaceHistory = connectionService.getInterfaceHistory(interfaceId);
        return ResponseEntity.ok(interfaceHistory.stream().map(connectionMapper::toConnectionHistoryResponseDto).toList());
    }

    @GetMapping("/ordinateur/{ordinateurId}/history")
    public ResponseEntity<List<ConnectionHistoryForPcResponseDto>> getPcHistory(
            @PathVariable UUID ordinateurId
    ) {
        List<Connection> pcHistory = connectionService.getPcHistory(ordinateurId);

        return ResponseEntity.ok(pcHistory.stream().map(connectionMapper::toConnectionHistoryForPcResponseDto).toList());
    }
}
