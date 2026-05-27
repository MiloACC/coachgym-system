package edu.co.ustavillavicencio.coachboard.entity;

import edu.co.ustavillavicencio.coachboard.enums.PlanSaaS;
import edu.co.ustavillavicencio.coachboard.enums.TipoDeporte;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "organizaciones")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Organizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre de la organización
    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    // Slug único de la organización
    @Column(nullable = false, unique = true, length = 50, updatable = false)
    private String slug;

    // Tipo de deporte de la organización
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDeporte tipoDeporte;

    // Plan SaaS contratado
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlanSaaS plan;

    // Estado de la organización
    @Column(nullable = false)
    private boolean activa;

    // Fecha de creación de la organización
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaDeCreacion;

    // Usuarios pertenecientes a la organización
    @OneToMany(mappedBy = "organizacion", fetch = FetchType.LAZY)
    private List<Usuario> usuarios;

    // Membresías de la organización
    @OneToMany(mappedBy = "organizacion", fetch = FetchType.LAZY)
    private List<Membresia> membresias;

    // Inicializar valores automáticos
    @PrePersist
    public void prePersist() {
        this.activa = true;
        this.fechaDeCreacion = LocalDateTime.now();
    }
}