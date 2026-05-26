package edu.co.ustavillavicencio.coachboard.controller;

import edu.co.ustavillavicencio.coachboard.dto.request.RegistrarPagoRequest;
import edu.co.ustavillavicencio.coachboard.dto.response.ApiResponse;
import edu.co.ustavillavicencio.coachboard.security.UserPrincipal;
import edu.co.ustavillavicencio.coachboard.service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    @PostMapping("/api/org/inscripciones/{inscripcionId}/pagos")
    @PreAuthorize("hasAnyRole('ORG_ADMIN', 'ORG_STAFF')")
    public ResponseEntity<ApiResponse<?>> registrar(@PathVariable Long inscripcionId,
                                                    @Valid @RequestBody RegistrarPagoRequest req,
                                                    @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Pago registrado", pagoService.registrar(inscripcionId, req, principal)));
    }

    @GetMapping("/api/org/inscripciones/{inscripcionId}/pagos")
    @PreAuthorize("hasAnyRole('ORG_ADMIN', 'ORG_STAFF')")
    public ResponseEntity<ApiResponse<?>> listarPorInscripcion(@PathVariable Long inscripcionId,
                                                               @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.ok(pagoService.listarPorInscripcion(inscripcionId, principal)));
    }

    @GetMapping("/api/org/pagos/pendientes")
    @PreAuthorize("hasAnyRole('ORG_ADMIN', 'ORG_STAFF')")
    public ResponseEntity<ApiResponse<?>> listarPendientes(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.ok(pagoService.listarPendientes(principal)));
    }

    @GetMapping("/api/org/metricas/ingresos")
    @PreAuthorize("hasRole('ORG_ADMIN')")
    public ResponseEntity<ApiResponse<?>> calcularIngresos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta,
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.ok(pagoService.calcularIngresos(desde, hasta, principal)));
    }

    @GetMapping("/api/cliente/pagos")
    @PreAuthorize("hasRole('ORG_CLIENTE')")
    public ResponseEntity<ApiResponse<?>> obtenerPropios(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.ok(pagoService.obtenerPropios(principal)));
    }
}