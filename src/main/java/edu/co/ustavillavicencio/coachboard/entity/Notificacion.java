package edu.co.ustavillavicencio.coachboard.entity;

import edu.co.ustavillavicencio.coachboard.enums.TipoDeNotificacion;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Tipo de notificación
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDeNotificacion tipo;

    // Mensaje de la notificación
    @Column(nullable = false, length = 255)
    private String mensaje;

    // Fecha de creación de la notificación
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaGeneracion;

    // Indica si la notificación fue leída
    @Column(nullable = false)
    private boolean leida;

    // Días restantes para vencimiento
    private Integer diasRestantes;

    // Cliente asociado a la notificación
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    // Inscripción asociada a la notificación
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inscripcion_id", nullable = false)
    private Inscripcion inscripcion;

    // Inicializar valores automáticos
    @PrePersist
    public void prePersist() {
        this.fechaGeneracion = LocalDateTime.now();
        this.leida = false;
    }
}