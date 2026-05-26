package edu.co.ustavillavicencio.coachboard.controller;

import edu.co.ustavillavicencio.coachboard.dto.request.ActualizarOrganizacionRequest;
import edu.co.ustavillavicencio.coachboard.dto.request.CrearAdminOrganizacionRequest;
import edu.co.ustavillavicencio.coachboard.dto.request.CrearOrganizacionRequest;
import edu.co.ustavillavicencio.coachboard.dto.response.ApiResponse;
import edu.co.ustavillavicencio.coachboard.service.OrganizacionService;
import edu.co.ustavillavicencio.coachboard.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/platform/organizaciones")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PLATFORM_ADMIN')")
public class OrganizacionController {

    private final OrganizacionService organizacionService;
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> crear(@Valid @RequestBody CrearOrganizacionRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Organización creada", organizacionService.crear(req)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(organizacionService.listar()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(organizacionService.obtener(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> actualizar(@PathVariable Long id,
                                                     @Valid @RequestBody ActualizarOrganizacionRequest req) {
        return ResponseEntity.ok(ApiResponse.ok("Organización actualizada", organizacionService.actualizar(id, req)));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<?>> toggleEstado(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Estado actualizado", organizacionService.toggleEstado(id)));
    }

    @PostMapping("/{orgId}/admins")
    public ResponseEntity<ApiResponse<?>> crearAdmin(@PathVariable Long orgId,
                                                     @Valid @RequestBody CrearAdminOrganizacionRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Admin creado", usuarioService.crearAdminOrganizacion(orgId, req)));
    }
}