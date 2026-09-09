package com.example.project.Domain.Dtos;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateSshConnectionRequestDto {
    @NotBlank(message = "username est obligatoire")
    private String username;
    @NotBlank(message = "password est obligatoire")
    private String password;

}
