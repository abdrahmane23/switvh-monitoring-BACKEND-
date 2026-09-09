package com.example.project.Services;


import com.example.project.Domain.Entities.Connection;

import java.util.List;
import java.util.UUID;

public interface ConnectionService {
    List<Connection> getPcHistory(UUID OrdinateurId);
    List<Connection> getInterfaceHistory(UUID interfaceId);
}
