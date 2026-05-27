package edu.co.ustavillavicencio.coachboard.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CrearMembresiaRequest {

    @NotBlank
    @Size(max = 80)
    private String nombre;

    @NotBlank
    private String descripcion;

    @Positive
    private int duracionDias;

    @NotNull
    @Positive
    private BigDecimal precio;
}