package com.example.project.Repositories;


import com.example.project.Domain.Entities.Utilisateur;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UtilisateurRepo extends JpaRepository<Utilisateur, UUID> {

     boolean existsByNomAndPrenom(String nom, String prenom);
}
