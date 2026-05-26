package edu.co.ustavillavicencio.coachboard.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CrearInscripcionRequest {

    @NotNull
    private Long clienteId;

    @NotNull
    private Long tipoMembresiaId;

    @NotNull
    private LocalDate fechaDeInicio;
}