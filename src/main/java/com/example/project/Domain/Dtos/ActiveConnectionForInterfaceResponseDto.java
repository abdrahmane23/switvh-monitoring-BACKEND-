package com.example.project.Domain.Dtos;

import com.example.project.Domain.Enums.CONNECTION_STATUS_ENUM;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiveConnectionForInterfaceResponseDto {
    private UUID id;
    private GetOrdinateurResponseDto ordinateur;
    private CONNECTION_STATUS_ENUM status;

    }

