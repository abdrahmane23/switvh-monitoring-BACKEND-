package com.example.project.Domain.Entities;

import com.example.project.Domain.Enums.INTERFACE_STATUS_ENUM;
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
public class Interface {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;
    @Column
    private String nom;
    @Column
    private INTERFACE_STATUS_ENUM status;

    @ManyToOne
    @JoinColumn(name = "vlan_id")
    private Vlan vlan;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    @OneToMany(mappedBy = "switchInterface", cascade = CascadeType.ALL,fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Connection> connections = new ArrayList<>();

    @OneToMany(mappedBy = "switchInterface", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Alert> alerts = new ArrayList<>();



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Interface interf = (Interface) o;

        return nom != null && nom.equals(interf.nom);
    }

    @Override
    public int hashCode() {
        return nom != null ? nom.hashCode() : 0;
    }


    @Override
    public String toString() {
        return "Interface{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", status=" + status +
                ", vlan=" + vlan +
                '}';
    }
}
