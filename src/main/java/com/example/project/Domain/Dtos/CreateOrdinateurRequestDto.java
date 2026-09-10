package com.example.project.Domain.Dtos;


import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrdinateurRequestDto {
    @NotBlank(message = "nom d'ordinateur est obligatoire")
    private String nom;
    @NotBlank(message = "marque d'ordinateur est obligatoire")
    private String marque;
    @Pattern(
            regexp = "^(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)){3}$",
            message = "Invalid IPv4 address"
    )
    private String addressIp ;

    private String macAdress;
}
