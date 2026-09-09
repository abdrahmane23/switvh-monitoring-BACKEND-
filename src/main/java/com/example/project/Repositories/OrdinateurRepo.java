package com.example.project.Repositories;

import com.example.project.Domain.Entities.Ordinateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface OrdinateurRepo extends JpaRepository<Ordinateur, UUID> {
    boolean existsByMacAdress(String macAdress);

    boolean existsByNom(String nom);

    Optional<Ordinateur> findByNom(String nom);

    Optional<Ordinateur> findByMacAdress(String macAdress);

    boolean existsByAddressIp(String addressIp);
}
