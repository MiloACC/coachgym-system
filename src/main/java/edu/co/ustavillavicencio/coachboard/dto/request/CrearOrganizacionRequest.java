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
public class CrearOrganizacionRequest {

    @NotBlank
    @Size(max = 100)
    private String nombre;

    @NotBlank
    @Size(max = 50)
    private String slug;

    @NotNull
    private TipoDeporte tipoDeporte;

    @NotNull
    private PlanSaaS plan;
}