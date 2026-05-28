package edu.co.ustavillavicencio.coachboard.dto.response;

import edu.co.ustavillavicencio.coachboard.enums.TipoDeNotificacion;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificacionResponse {

    private Long id;
    private TipoDeNotificacion tipo;
    private String mensaje;
    private LocalDateTime fechaGeneracion;
    private boolean leida;
    private Integer diasRestantes;
    private Long clienteId;
    private Long inscripcionId;
}