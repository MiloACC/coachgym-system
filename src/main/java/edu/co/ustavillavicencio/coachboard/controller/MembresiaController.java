package edu.co.ustavillavicencio.coachboard.controller;

import edu.co.ustavillavicencio.coachboard.dto.request.ActualizarMembresiaRequest;
import edu.co.ustavillavicencio.coachboard.dto.request.CrearMembresiaRequest;
import edu.co.ustavillavicencio.coachboard.dto.response.ApiResponse;
import edu.co.ustavillavicencio.coachboard.security.UserPrincipal;
import edu.co.ustavillavicencio.coachboard.service.MembresiaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/org/membresias")
@RequiredArgsConstructor
public class MembresiaController {

    private final MembresiaService membresiaService;

    @PostMapping
    @PreAuthorize("hasRole('ORG_ADMIN')")
    public ResponseEntity<ApiResponse<?>> crear(@Valid @RequestBody CrearMembresiaRequest req,
                                                @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Membresía creada", membresiaService.crear(req, principal)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ORG_ADMIN', 'ORG_STAFF')")
    public ResponseEntity<ApiResponse<?>> listar(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.ok(membresiaService.listar(principal)));
    }

    @GetMapping("/activas")
    @PreAuthorize("hasAnyRole('ORG_ADMIN', 'ORG_STAFF')")
    public ResponseEntity<ApiResponse<?>> listarActivas(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.ok(membresiaService.listarActivas(principal)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ORG_ADMIN', 'ORG_STAFF')")
    public ResponseEntity<ApiResponse<?>> obtener(@PathVariable Long id,
                                                  @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.ok(membresiaService.obtener(id, principal)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ORG_ADMIN')")
    public ResponseEntity<ApiResponse<?>> actualizar(@PathVariable Long id,
                                                     @Valid @RequestBody ActualizarMembresiaRequest req,
                                                     @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.ok("Membresía actualizada", membresiaService.actualizar(id, req, principal)));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ORG_ADMIN')")
    public ResponseEntity<ApiResponse<?>> toggleEstado(@PathVariable Long id,
                                                       @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.ok("Estado actualizado", membresiaService.toggleEstado(id, principal)));
    }
}