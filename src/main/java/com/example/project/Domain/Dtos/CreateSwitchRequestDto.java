package com.example.project.Domain.Dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CreateSwitchRequestDto {
    @NotBlank
    private String nom;
    private String ip;
    @Valid
    private CreateSshConnectionRequestDto ssh;
    @Valid
    @NotNull
    private CreateTelnetConnectionRequestDto telnet;
}
