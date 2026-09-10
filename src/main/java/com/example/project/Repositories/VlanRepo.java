package com.example.project.Repositories;

import com.example.project.Domain.Entities.Switch;
import com.example.project.Domain.Entities.Vlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VlanRepo extends JpaRepository<Vlan, UUID> {
}
