package edu.co.ustavillavicencio.coachboard.controller;

import edu.co.ustavillavicencio.coachboard.dto.request.ActualizarClienteRequest;
import edu.co.ustavillavicencio.coachboard.dto.request.CrearClienteRequest;
import edu.co.ustavillavicencio.coachboard.dto.response.ApiResponse;
import edu.co.ustavillavicencio.coachboard.security.UserPrincipal;
import edu.co.ustavillavicencio.coachboard.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping("/api/org/clientes")
    @PreAuthorize("hasAnyRole('ORG_ADMIN', 'ORG_STAFF')")
    public ResponseEntity<ApiResponse<?>> registrar(@Valid @RequestBody CrearClienteRequest req,
                                                    @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Cliente registrado", clienteService.registrar(req, principal)));
    }

    @GetMapping("/api/org/clientes")
    @PreAuthorize("hasAnyRole('ORG_ADMIN', 'ORG_STAFF')")
    public ResponseEntity<ApiResponse<?>> listar(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.ok(clienteService.listar(principal)));
    }

    @GetMapping("/api/org/clientes/{id}")
    @PreAuthorize("hasAnyRole('ORG_ADMIN', 'ORG_STAFF')")
    public ResponseEntity<ApiResponse<?>> obtener(@PathVariable Long id,
                                                  @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.ok(clienteService.obtener(id, principal)));
    }

    @PutMapping("/api/org/clientes/{id}")
    @PreAuthorize("hasAnyRole('ORG_ADMIN', 'ORG_STAFF')")
    public ResponseEntity<ApiResponse<?>> actualizar(@PathVariable Long id,
                                                     @Valid @RequestBody ActualizarClienteRequest req,
                                                     @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.ok("Cliente actualizado", clienteService.actualizar(id, req, principal)));
    }

    @GetMapping("/api/org/clientes/buscar")
    @PreAuthorize("hasAnyRole('ORG_ADMIN', 'ORG_STAFF')")
    public ResponseEntity<ApiResponse<?>> buscar(@RequestParam String nombre,
                                                 @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.ok(clienteService.buscar(nombre, principal)));
    }

    @GetMapping("/api/cliente/perfil")
    @PreAuthorize("hasRole('ORG_CLIENTE')")
    public ResponseEntity<ApiResponse<?>> obtenerPropio(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.ok(clienteService.obtenerPropio(principal)));
    }
}