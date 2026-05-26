package edu.co.ustavillavicencio.coachboard.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class MembresiaResponse {

    private Long id;
    private String nombre;
    private String descripcion;
    private int duracionDias;
    private BigDecimal precio;
    private boolean activo;
    private Long organizacionId;
}