package edu.co.ustavillavicencio.coachboard.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearClienteRequest {

    @NotBlank
    @Size(max = 100)
    private String nombre;

    @NotBlank
    @Size(max = 10)
    private String celular;

    @NotBlank
    @Size(max = 10)
    private String documento;

    @NotBlank
    @Size(max = 50)
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    @Email
    private String email;
}