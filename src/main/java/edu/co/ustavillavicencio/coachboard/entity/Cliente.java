package edu.co.ustavillavicencio.coachboard.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "clientes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre completo del cliente
    @Column(nullable = false, length = 100)
    private String nombre;

    // Número de celular del cliente
    @Column(nullable = false, unique = true, length = 10)
    private String celular;

    // Documento de identidad del cliente
    @Column(nullable = false, unique = true, length = 10)
    private String documento;

    // Fecha de registro del cliente
    @Column(nullable = false, updatable = false)
    private LocalDate fechaDeRegistro;

    // Usuario asociado al cliente
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    // Historial de inscripciones del cliente
    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    private List<Inscripcion> inscripciones;

    // Notificaciones del cliente
    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    private List<Notificacion> notificaciones;

    // Asignar fecha de registro automáticamente
    @PrePersist
    public void prePersist() {
        this.fechaDeRegistro = LocalDate.now();
    }
}