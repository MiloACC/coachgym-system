package edu.co.ustavillavicencio.coachboard.service;

import edu.co.ustavillavicencio.coachboard.dto.response.NotificacionResponse;
import edu.co.ustavillavicencio.coachboard.entity.Notificacion;
import edu.co.ustavillavicencio.coachboard.exception.ResourceNotFoundException;
import edu.co.ustavillavicencio.coachboard.repository.ClienteRepository;
import edu.co.ustavillavicencio.coachboard.repository.NotificacionRepository;
import edu.co.ustavillavicencio.coachboard.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final ClienteRepository clienteRepository;

    @Transactional(readOnly = true)
    public List<NotificacionResponse> obtenerPropias(UserPrincipal principal) {
        Long clienteId = clienteRepository.findByUsuario_Id(principal.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("Perfil de cliente no encontrado"))
            .getId();
        return notificacionRepository.findByCliente_IdOrderByFechaGeneracionDesc(clienteId)
            .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public long contarNoLeidas(UserPrincipal principal) {
        Long clienteId = clienteRepository.findByUsuario_Id(principal.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("Perfil de cliente no encontrado"))
            .getId();
        return notificacionRepository.countByCliente_IdAndLeida(clienteId, false);
    }

    @Transactional(readOnly = true)
    public List<NotificacionResponse> listarNoLeidasOrg(UserPrincipal principal) {
        return notificacionRepository.findNoLeidasPorOrganizacion(principal.getOrganizacionId())
            .stream().map(this::toResponse).toList();
    }

    @Transactional
    public void marcarComoLeida(Long id, UserPrincipal principal) {
        Notificacion n = notificacionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Notificación no encontrada"));
        if (!n.getCliente().getUsuario().getId().equals(principal.getUserId()))
            throw new ResourceNotFoundException("Notificación no encontrada");
        n.setLeida(true);
        notificacionRepository.save(n);
    }

    @Transactional
    public void marcarTodasComoLeidas(UserPrincipal principal) {
        Long clienteId = clienteRepository.findByUsuario_Id(principal.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("Perfil de cliente no encontrado"))
            .getId();
        notificacionRepository.marcarTodasComoLeidas(clienteId);
    }

    private NotificacionResponse toResponse(Notificacion n) {
        return NotificacionResponse.builder()
            .id(n.getId())
            .tipo(n.getTipo())
            .mensaje(n.getMensaje())
            .fechaGeneracion(n.getFechaGeneracion())
            .leida(n.isLeida())
            .diasRestantes(n.getDiasRestantes())
            .clienteId(n.getCliente().getId())
            .inscripcionId(n.getInscripcion().getId())
            .build();
    }
}