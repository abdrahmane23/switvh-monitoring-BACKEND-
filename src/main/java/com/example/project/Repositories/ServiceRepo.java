package com.example.project.Repositories;

import com.example.project.Domain.Entities.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface ServiceRepo extends JpaRepository<Service, UUID> {
    Optional<Service> findByNom(String nom);

}
