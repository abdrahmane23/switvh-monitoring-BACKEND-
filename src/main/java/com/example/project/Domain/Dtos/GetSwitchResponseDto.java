package com.example.project.Domain.Dtos;


import com.example.project.Domain.Enums.SWITCH_STATUS_ENUM;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetSwitchResponseDto {
    private UUID id;
    private String nom;
    private SWITCH_STATUS_ENUM status;
}
