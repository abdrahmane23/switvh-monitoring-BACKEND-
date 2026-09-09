package com.example.project.Domain.Dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetOrdinateurResponseDto {
    private UUID id;
    private String nom;
    private String addressIp;
    private String macAdress;
}
