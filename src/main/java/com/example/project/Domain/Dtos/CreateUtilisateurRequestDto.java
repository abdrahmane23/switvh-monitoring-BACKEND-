package com.example.project.Domain.Dtos;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUtilisateurRequestDto {

    @NotBlank(message = "le nom est obligatoire")
    private String nom;

    @NotBlank(message = "le prenom est obligatoire")
    private String prenom;

    @NotBlank(message = "email est obligatoire")
    @Email(message = "email non-valide ")
    private String email;

    @Pattern(
            regexp = "^\\+[1-9]\\d{7,14}$",
            message = "telephone non-valide, doit commencer par un '+' suivi de 8 à 15 chiffres"
    )
    private String telephone;
}
