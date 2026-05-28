package edu.co.ustavillavicencio.coachboard.dto.request;

import edu.co.ustavillavicencio.coachboard.enums.PlanSaaS;
import edu.co.ustavillavicencio.coachboard.enums.TipoDeporte;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    // Organización
    @NotBlank
    @Size(max = 100)
    private String nombreOrganizacion;

    @NotBlank
    @Size(max = 50)
    private String slug;

    @NotNull
    private TipoDeporte tipoDeporte;

    @NotNull
    private PlanSaaS plan;

    // Admin de la organización
    @NotBlank
    @Size(max = 50)
    private String username;

    @NotBlank
    @Size(min = 8)
    private String password;

    @NotBlank
    @Email
    private String email;
}
