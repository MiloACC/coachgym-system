package edu.co.ustavillavicencio.coachboard.entity;

import edu.co.ustavillavicencio.coachboard.enums.EstadoDelPago;
import edu.co.ustavillavicencio.coachboard.enums.MetodoDePago;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Valor del pago
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    // Fecha y hora del pago
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaDePago;

    // Método de pago utilizado
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "metodo_de_pago")
    private MetodoDePago metodoDePago;

    // Estado actual del pago
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "estado_del_pago")
    private EstadoDelPago estadoDelPago;

    // Inscripción asociada al pago
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inscripcion_id", nullable = false)
    private Inscripcion inscripcion;

    // Usuario que registró el pago
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registrado_por_id", nullable = false)
    private Usuario registradoPor;

    // Asignar fecha automáticamente al crear el pago
    @PrePersist
    public void prePersist() {
        this.fechaDePago = LocalDateTime.now();
    }
}