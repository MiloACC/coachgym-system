package edu.co.ustavillavicencio.coachboard.controller;

import edu.co.ustavillavicencio.coachboard.dto.request.CrearStaffRequest;
import edu.co.ustavillavicencio.coachboard.dto.response.ApiResponse;
import edu.co.ustavillavicencio.coachboard.security.UserPrincipal;
import edu.co.ustavillavicencio.coachboard.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/org/usuarios")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ORG_ADMIN')")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/staff")
    public ResponseEntity<ApiResponse<?>> crearStaff(@Valid @RequestBody CrearStaffRequest req,
                                                     @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Staff creado", usuarioService.crearStaff(req, principal)));
    }

    @GetMapping("/staff")
    public ResponseEntity<ApiResponse<?>> listarStaff(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.ok(usuarioService.listarStaff(principal)));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<?>> toggleEstado(@PathVariable Long id,
                                                       @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.ok("Estado actualizado", usuarioService.toggleEstado(id, principal)));
    }
}