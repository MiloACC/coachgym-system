package edu.co.ustavillavicencio.coachboard.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ClienteResponse {

    private Long id;
    private String nombre;
    private String celular;
    private String documento;
    private LocalDate fechaDeRegistro;
    private Long usuarioId;
    private String email;
}