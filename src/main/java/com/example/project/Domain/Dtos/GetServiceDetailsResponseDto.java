package com.example.project.Domain.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetServiceDetailsResponseDto {
    private UUID id;
    private String nom;
    private String emplacement;
    private List<OrdinateurResponseDto> ordinateurs= new ArrayList<>();
}
