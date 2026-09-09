package com.example.project.Domain.Dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiveAlert {
    private UUID Id;
    private String macAddress;
    private LocalDateTime createdAt;
}
