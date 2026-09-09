package com.example.project.Repositories;


import com.example.project.Domain.Entities.Interface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InterfaceRepo extends JpaRepository<Interface, UUID> {
    Interface findByNom(String port);
}
