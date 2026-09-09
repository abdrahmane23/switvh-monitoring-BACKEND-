package com.example.project.Domain.Entities;


import com.example.project.Domain.Enums.SWITCH_STATUS_ENUM;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Switch {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;
    @Column
    private String nom;
    @Column
    private String ip;
    @Column
    private SWITCH_STATUS_ENUM status;

    @OneToMany(mappedBy = "switchEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    List<Vlan> vlans = new ArrayList<>();

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "ssh_connection_id")
    private SshConnection sshConnection ;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "telnet_connection_id")
    private TelnetConnection telnetConnection;

}
