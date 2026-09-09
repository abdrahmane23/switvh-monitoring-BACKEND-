package com.example.project.Controllers;


import com.example.project.Domain.Dtos.CreateOrdinateurRequestDto;
import com.example.project.Domain.Dtos.CreateUtilisateurRequestDto;
import com.example.project.Domain.Dtos.GetOrdinateurDetailsResponeDto;
import com.example.project.Domain.Entities.Connection;
import com.example.project.Domain.Entities.Ordinateur;
import com.example.project.Mapper.ConnectionMapper;
import com.example.project.Mapper.OrdinateurMapper;
import com.example.project.Services.OrdinateurService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OrdinateurController {
    private final OrdinateurService ordinateurService;
    private final OrdinateurMapper ordinateurMapper;

    @PostMapping("api/Services/{serviceId}/ordinateur")
    public ResponseEntity<Void> createPc(
            @PathVariable UUID serviceId,
            @Valid @RequestBody CreateOrdinateurRequestDto createOrdinateurRequestDto
    ) {
        ordinateurService.createPc(createOrdinateurRequestDto, serviceId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("api/ordinateur/{ordinateurId}")
    public ResponseEntity<Void> editPc(
            @PathVariable UUID ordinateurId,
            @Valid @RequestBody CreateOrdinateurRequestDto createOrdinateurRequestDto
    ) {
        ordinateurService.editPc(createOrdinateurRequestDto, ordinateurId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("api/ordinateur/{ordinateurId}")
    public ResponseEntity<GetOrdinateurDetailsResponeDto> getPcDetails(
            @PathVariable UUID ordinateurId
    ) {
        Ordinateur ordinateur = ordinateurService.getPc(ordinateurId);
        return ResponseEntity.ok(ordinateurMapper.toDto(ordinateur));
    }

    @DeleteMapping("api/ordinateur/{ordinateurId}")
    public ResponseEntity<Void> deletePc(
            @PathVariable UUID ordinateurId
    ) {
        ordinateurService.deletePc(ordinateurId);
        return ResponseEntity.ok().build();
    }
}

