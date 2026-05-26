package edu.co.ustavillavicencio.coachboard.dto.request;

import edu.co.ustavillavicencio.coachboard.enums.PlanSaaS;
import edu.co.ustavillavicencio.coachboard.enums.TipoDeporte;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarOrganizacionRequest {

    @NotBlank
    @Size(max = 100)
    private String nombre;

    @NotNull
    private TipoDeporte tipoDeporte;

    @NotNull
    private PlanSaaS plan;
}