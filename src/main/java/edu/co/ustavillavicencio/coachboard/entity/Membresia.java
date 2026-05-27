package edu.co.ustavillavicencio.coachboard.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "membresias")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Membresia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre de la membresía
    @Column(nullable = false, length = 80)
    private String nombre;

    // Descripción de la membresía
    @Column(nullable = false)
    private String descripcion;

    // Duración de la membresía en días
    @Column(nullable = false)
    private int duracionDias;

    // Precio de la membresía
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    // Indica si la membresía está activa
    @Column(nullable = false)
    private boolean activo;

    // Organización dueña de la membresía
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacion_id", nullable = false)
    private Organizacion organizacion;

    // Inscripciones asociadas a esta membresía
    @OneToMany(mappedBy = "tipoDeMembresia", fetch = FetchType.LAZY)
    private List<Inscripcion> inscripciones;

    // Activar membresía automáticamente al crearla
    @PrePersist
    public void prePersist() {
        this.activo = true;
    }
}