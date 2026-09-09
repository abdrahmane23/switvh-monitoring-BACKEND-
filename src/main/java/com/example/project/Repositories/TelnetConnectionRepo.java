package com.example.project.Repositories;


import com.example.project.Domain.Entities.TelnetConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TelnetConnectionRepo extends JpaRepository<TelnetConnection, UUID> {
}
