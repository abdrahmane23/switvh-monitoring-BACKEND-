package com.example.project.Repositories;

import com.example.project.Domain.Entities.SshConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SshconnectionRepo extends JpaRepository<SshConnection, UUID> {
}
