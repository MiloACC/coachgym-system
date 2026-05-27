package edu.co.ustavillavicencio.coachboard.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UsuarioResponse {

    private Long id;
    private String username;
    private String email;
    private String rol;
    private boolean activo;
    private LocalDateTime fechaDeCreacion;
    private Long organizacionId;
    private String nombreOrganizacion;
}