package edu.co.ustavillavicencio.coachboard.entity;

import edu.co.ustavillavicencio.coachboard.enums.EstadoInscripcion;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "inscripciones")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Fecha de inicio de la membresía
    @Column(nullable = false, updatable = false)
    private LocalDate fechaDeInicio;

    // Fecha de vencimiento de la membresía
    @Column(nullable = false)
    private LocalDate fechaDeFin;

    // Estado actual de la inscripción
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoInscripcion estado;

    // Cliente asociado a la inscripción
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    // Membresía asociada a la inscripción
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_membresia_id", nullable = false)
    private Membresia tipoDeMembresia;

    // Usuario que registró la inscripción
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creado_por_id", nullable = false)
    private Usuario creadoPor;

    // Pagos realizados para esta inscripción
    @OneToMany(mappedBy = "inscripcion", fetch = FetchType.LAZY)
    private List<Pago> pagos;

    // Notificaciones relacionadas con la inscripción
    @OneToMany(mappedBy = "inscripcion", fetch = FetchType.LAZY)
    private List<Notificacion> notificaciones;
}