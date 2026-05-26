package edu.co.ustavillavicencio.coachboard.dto.request;

import edu.co.ustavillavicencio.coachboard.enums.MetodoDePago;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RegistrarPagoRequest {

    @NotNull
    @Positive
    private BigDecimal monto;

    @NotNull
    private MetodoDePago metodoDePago;
}