package edu.co.ustavillavicencio.coachboard.controller;

import edu.co.ustavillavicencio.coachboard.dto.request.LoginRequest;
import edu.co.ustavillavicencio.coachboard.dto.response.ApiResponse;
import edu.co.ustavillavicencio.coachboard.dto.response.JwtResponse;
import edu.co.ustavillavicencio.coachboard.security.UserPrincipal;
import edu.co.ustavillavicencio.coachboard.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(ApiResponse.ok("Login exitoso", usuarioService.login(req)));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<?>> perfil(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.ok(usuarioService.obtenerPerfil(principal)));
    }
}