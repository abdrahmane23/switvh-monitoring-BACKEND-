package com.example.project.Services;

import com.example.project.Domain.Dtos.CreateOrdinateurRequestDto;
import com.example.project.Domain.Dtos.CreateUtilisateurRequestDto;
import com.example.project.Domain.Entities.Connection;
import com.example.project.Domain.Entities.Ordinateur;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface OrdinateurService {
     void createPc(CreateOrdinateurRequestDto createOrdinateurRequestDto, UUID serviceId);

    void editPc(CreateOrdinateurRequestDto createOrdinateurRequestDto,  UUID ordinateurId);

    Ordinateur getPc(UUID ordinateurId);

    void deletePc(UUID ordinateurId);

    void movePc(UUID serviceId, UUID ordinateurId);

}
