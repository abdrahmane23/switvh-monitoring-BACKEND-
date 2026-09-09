
package com.example.project.Domain.Dtos;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTelnetConnectionRequestDto {
    @NotBlank(message = "password est obligatoire")
    private String password;

}
