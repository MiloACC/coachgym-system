package edu.co.ustavillavicencio.coachboard.controller;

import edu.co.ustavillavicencio.coachboard.dto.response.ApiResponse;
import edu.co.ustavillavicencio.coachboard.security.UserPrincipal;
import edu.co.ustavillavicencio.coachboard.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;

    @GetMapping("/api/cliente/notificaciones")
    @PreAuthorize("hasRole('ORG_CLIENTE')")
    public ResponseEntity<ApiResponse<?>> obtenerPropias(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.ok(notificacionService.obtenerPropias(principal)));
    }

    @GetMapping("/api/cliente/notificaciones/no-leidas/count")
    @PreAuthorize("hasRole('ORG_CLIENTE')")
    public ResponseEntity<ApiResponse<?>> contarNoLeidas(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.ok(notificacionService.contarNoLeidas(principal)));
    }

    @PatchMapping("/api/cliente/notificaciones/{id}/leida")
    @PreAuthorize("hasRole('ORG_CLIENTE')")
    public ResponseEntity<ApiResponse<?>> marcarComoLeida(@PathVariable Long id,
                                                          @AuthenticationPrincipal UserPrincipal principal) {
        notificacionService.marcarComoLeida(id, principal);
        return ResponseEntity.ok(ApiResponse.ok("Notificación marcada como leída", null));
    }

    @PatchMapping("/api/cliente/notificaciones/leidas")
    @PreAuthorize("hasRole('ORG_CLIENTE')")
    public ResponseEntity<ApiResponse<?>> marcarTodasComoLeidas(@AuthenticationPrincipal UserPrincipal principal) {
        notificacionService.marcarTodasComoLeidas(principal);
        return ResponseEntity.ok(ApiResponse.ok("Notificaciones marcadas como leídas", null));
    }

    @GetMapping("/api/org/notificaciones")
    @PreAuthorize("hasAnyRole('ORG_ADMIN', 'ORG_STAFF')")
    public ResponseEntity<ApiResponse<?>> listarNoLeidasOrg(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.ok(notificacionService.listarNoLeidasOrg(principal)));
    }
}