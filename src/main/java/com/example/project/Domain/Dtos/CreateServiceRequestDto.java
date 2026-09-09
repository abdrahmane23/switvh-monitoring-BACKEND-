package com.example.project.Domain.Dtos;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateServiceRequestDto {
    @NotBlank(message = "le Nom du service est obligatoire")
    private String nom;
    @NotBlank(message = "l'emplacement'est obligatoire")
    private String emplacement;
}
