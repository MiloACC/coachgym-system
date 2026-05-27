package edu.co.ustavillavicencio.coachboard.dto.response;

import edu.co.ustavillavicencio.coachboard.enums.EstadoDelPago;
import edu.co.ustavillavicencio.coachboard.enums.MetodoDePago;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class PagoResponse {

    private Long id;
    private BigDecimal monto;
    private LocalDateTime fechaDePago;
    private MetodoDePago metodoDePago;
    private EstadoDelPago estadoDelPago;
    private Long inscripcionId;
    private Long registradoPorId;
}