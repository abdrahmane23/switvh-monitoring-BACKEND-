package com.example.project.Domain.Entities;


import com.example.project.Domain.Enums.VLAN_STATUS_ENUM;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DialectOverride;
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
public class Vlan {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;
    @Column
    private int numero;
    @Column(unique = true)
    private String nom;
    @Column
    private VLAN_STATUS_ENUM status;

    @OneToMany(mappedBy = "vlan", cascade = CascadeType.ALL,fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Interface> interfaces = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "switch_id")
    private Switch switchEntity;


    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vlan vlan = (Vlan) o;

        return nom != null && nom.equals(vlan.nom);
    }

    @Override
    public int hashCode() {
        return nom != null ? nom.hashCode() : 0;
    }
}
