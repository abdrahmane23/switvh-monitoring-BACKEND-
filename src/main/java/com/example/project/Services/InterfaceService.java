package com.example.project.Services;


import com.example.project.Domain.Entities.Connection;
import com.example.project.Domain.Entities.Interface;

import java.util.List;
import java.util.UUID;

public interface InterfaceService {
    List<Interface> getInterfaces(UUID vlanId);

    Interface getInterfaceDetails(UUID interfaceId);

}
