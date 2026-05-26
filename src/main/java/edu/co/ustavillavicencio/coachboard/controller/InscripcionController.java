package edu.co.ustavillavicencio.coachboard.controller;

import edu.co.ustavillavicencio.coachboard.dto.request.CrearInscripcionRequest;
import edu.co.ustavillavicencio.coachboard.dto.response.ApiResponse;
import edu.co.ustavillavicencio.coachboard.enums.EstadoInscripcion;
import edu.co.ustavillavicencio.coachboard.security.UserPrincipal;
import edu.co.ustavillavicencio.coachboard.service.InscripcionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class InscripcionController {

    private final InscripcionService inscripcionService;

    @PostMapping("/api/org/inscripciones")
    @PreAuthorize("hasAnyRole('ORG_ADMIN', 'ORG_STAFF')")
    public ResponseEntity<ApiResponse<?>> crear(@Valid @RequestBody CrearInscripcionRequest req,
                                                @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Inscripción creada", inscripcionService.crear(req, principal)));
    }

    @GetMapping("/api/org/inscripciones")
    @PreAuthorize("hasAnyRole('ORG_ADMIN', 'ORG_STAFF')")
    public ResponseEntity<ApiResponse<?>> listar(@RequestParam(required = false) EstadoInscripcion estado,
                                                 @AuthenticationPrincipal UserPrincipal principal) {
        if (estado != null)
            return ResponseEntity.ok(ApiResponse.ok(inscripcionService.listarPorEstado(estado, principal)));
        return ResponseEntity.ok(ApiResponse.ok(inscripcionService.listar(principal)));
    }

    @GetMapping("/api/org/inscripciones/{id}")
    @PreAuthorize("hasAnyRole('ORG_ADMIN', 'ORG_STAFF')")
    public ResponseEntity<ApiResponse<?>> obtener(@PathVariable Long id,
                                                  @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.ok(inscripcionService.obtener(id, principal)));
    }

    @GetMapping("/api/org/clientes/{clienteId}/inscripciones")
    @PreAuthorize("hasAnyRole('ORG_ADMIN', 'ORG_STAFF')")
    public ResponseEntity<ApiResponse<?>> historialCliente(@PathVariable Long clienteId,
                                                           @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.ok(inscripcionService.historialCliente(clienteId, principal)));
    }

    @PatchMapping("/api/org/inscripciones/{id}/estado")
    @PreAuthorize("hasRole('ORG_ADMIN')")
    public ResponseEntity<ApiResponse<?>> cambiarEstado(@PathVariable Long id,
                                                        @RequestParam EstadoInscripcion estado,
                                                        @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.ok("Estado actualizado", inscripcionService.cambiarEstado(id, estado, principal)));
    }

    @GetMapping("/api/cliente/inscripciones")
    @PreAuthorize("hasRole('ORG_CLIENTE')")
    public ResponseEntity<ApiResponse<?>> obtenerPropias(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.ok(inscripcionService.obtenerPropias(principal)));
    }
}