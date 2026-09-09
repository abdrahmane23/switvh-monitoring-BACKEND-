package com.example.project.Domain.Dtos;

import com.example.project.Domain.Enums.CONNECTION_STATUS_ENUM;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiveConnectionForPcResponseDto {
    private UUID id;
    private GetInterfaceResponseDto switchInterface;
    private CONNECTION_STATUS_ENUM status;

    }

