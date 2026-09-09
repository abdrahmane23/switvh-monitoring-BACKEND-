package com.example.project.Domain.Dtos;


import com.example.project.Domain.Enums.VLAN_STATUS_ENUM;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetVlanResponseDto {
    private UUID id;
    private int numero;
    private String nom;
    private VLAN_STATUS_ENUM status;
}
