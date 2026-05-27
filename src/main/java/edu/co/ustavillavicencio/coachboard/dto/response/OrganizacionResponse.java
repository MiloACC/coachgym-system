package edu.co.ustavillavicencio.coachboard.dto.response;

import edu.co.ustavillavicencio.coachboard.enums.PlanSaaS;
import edu.co.ustavillavicencio.coachboard.enums.TipoDeporte;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrganizacionResponse {

    private Long id;
    private String nombre;
    private String slug;
    private TipoDeporte tipoDeporte;
    private PlanSaaS plan;
    private boolean activa;
    private LocalDateTime fechaDeCreacion;
}