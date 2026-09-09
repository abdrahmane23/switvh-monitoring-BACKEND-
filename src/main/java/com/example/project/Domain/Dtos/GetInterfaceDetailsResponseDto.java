package com.example.project.Domain.Dtos;


import com.example.project.Domain.Enums.INTERFACE_STATUS_ENUM;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetInterfaceDetailsResponseDto {
    private UUID id;
    private String nom;
    private INTERFACE_STATUS_ENUM status;
    private ActiveConnectionForInterfaceResponseDto connection;
    private ActiveAlert alert;
}
