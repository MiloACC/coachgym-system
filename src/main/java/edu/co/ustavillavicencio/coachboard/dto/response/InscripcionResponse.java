package edu.co.ustavillavicencio.coachboard.dto.response;

import edu.co.ustavillavicencio.coachboard.enums.EstadoInscripcion;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class InscripcionResponse {

    private Long id;
    private LocalDate fechaDeInicio;
    private LocalDate fechaDeFin;
    private EstadoInscripcion estado;
    private Long clienteId;
    private String nombreCliente;
    private Long tipoMembresiaId;
    private String nombreMembresia;
    private BigDecimal precioMembresia;
    private Long creadoPorId;
}