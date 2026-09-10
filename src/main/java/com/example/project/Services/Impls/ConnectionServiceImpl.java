package com.example.project.Services.Impls;

import com.example.project.Domain.Entities.Connection;
import com.example.project.Domain.Entities.Interface;
import com.example.project.Domain.Entities.Ordinateur;
import com.example.project.Domain.Enums.CONNECTION_STATUS_ENUM;
import com.example.project.Exceptions.InterfaceNotFoundException;
import com.example.project.Exceptions.OrdinateurNotFoundException;
import com.example.project.Repositories.InterfaceRepo;
import com.example.project.Repositories.OrdinateurRepo;
import com.example.project.Services.ConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class ConnectionServiceImpl implements ConnectionService {
    private final OrdinateurRepo ordinateurRepo;
    private final InterfaceRepo interfaceRepo;
    @Override
    public List<Connection> getPcHistory(UUID ordinateurId) {
        Ordinateur ordinateur = ordinateurRepo.findById(ordinateurId).orElseThrow(
                () -> new OrdinateurNotFoundException("Ordinateur n'existe pas")
        );
        List<Connection> connections = ordinateur.getConnections()
                .stream()
                .filter(
                        c-> c.getStatus() == CONNECTION_STATUS_ENUM.INACTIVE
                ).toList();

        return connections ;
    }
    @Override
    public List<Connection> getInterfaceHistory(UUID interfaceId) {
        Interface interfaceDetails = interfaceRepo.findById(interfaceId).orElseThrow(
                () -> new InterfaceNotFoundException("Interface n'existe pas")
        );

        List<Connection> inActiveConnections = interfaceDetails.getConnections()
                .stream()
                .filter(
                        c-> c.getStatus() == CONNECTION_STATUS_ENUM.INACTIVE
                ).toList();

        return inActiveConnections ;
    }
}
