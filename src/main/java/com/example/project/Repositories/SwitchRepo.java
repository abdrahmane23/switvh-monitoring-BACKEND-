package com.example.project.Repositories;

import com.example.project.Domain.Entities.Ordinateur;
import com.example.project.Domain.Entities.Switch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SwitchRepo extends JpaRepository<Switch, UUID> {
    boolean existsByIp(String ip);
    boolean existsByNom(String nom);

    Optional<Switch> findByIp(String ip);

    Optional<Switch> findByNom(String nom);
}
