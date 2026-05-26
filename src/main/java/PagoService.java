import edu.co.ustavillavicencio.coachboard.dto.request.RegistrarPagoRequest;
import edu.co.ustavillavicencio.coachboard.dto.response.PagoResponse;
import edu.co.ustavillavicencio.coachboard.entity.Cliente;
import edu.co.ustavillavicencio.coachboard.entity.Inscripcion;
import edu.co.ustavillavicencio.coachboard.entity.Pago;
import edu.co.ustavillavicencio.coachboard.entity.Usuario;
import edu.co.ustavillavicencio.coachboard.enums.EstadoDelPago;
import edu.co.ustavillavicencio.coachboard.enums.EstadoInscripcion;
import edu.co.ustavillavicencio.coachboard.exception.BusinessException;
import edu.co.ustavillavicencio.coachboard.exception.ResourceNotFoundException;
import edu.co.ustavillavicencio.coachboard.repository.ClienteRepository;
import edu.co.ustavillavicencio.coachboard.repository.PagoRepository;
import edu.co.ustavillavicencio.coachboard.repository.UsuarioRepository;
import edu.co.ustavillavicencio.coachboard.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository pagoRepository;
    private final InscripcionService inscripcionService;
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;

    @Transactional
    public PagoResponse registrar(Long inscripcionId, RegistrarPagoRequest req, UserPrincipal principal) {
        Inscripcion inscripcion = inscripcionService.findOrThrow(inscripcionId);
        if (!inscripcion.getCliente().getUsuario().getOrganizacion().getId().equals(principal.getOrganizacionId()))
            throw new BusinessException("La inscripción no pertenece a su organización");
        if (inscripcion.getEstado() == EstadoInscripcion.CANCELADA ||
                inscripcion.getEstado() == EstadoInscripcion.VENCIDA)
            throw new BusinessException("No se puede registrar pago para una inscripción " + inscripcion.getEstado());

        Usuario registradoPor = usuarioRepository.findById(principal.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Pago pago = Pago.builder()
            .monto(req.getMonto())
            .metodoDePago(req.getMetodoDePago())
            .estadoDelPago(EstadoDelPago.COMPLETADO)
            .inscripcion(inscripcion)
            .registradoPor(registradoPor)
            .build();

        if (inscripcion.getEstado() == EstadoInscripcion.PENDIENTE_PAGO) {
            inscripcion.setEstado(EstadoInscripcion.ACTIVA);
        }

        return toResponse(pagoRepository.save(pago));
    }

    @Transactional(readOnly = true)
    public List<PagoResponse> listarPorInscripcion(Long inscripcionId, UserPrincipal principal) {
        Inscripcion inscripcion = inscripcionService.findOrThrow(inscripcionId);
        if (!inscripcion.getCliente().getUsuario().getOrganizacion().getId().equals(principal.getOrganizacionId()))
            throw new BusinessException("La inscripción no pertenece a su organización");
        return pagoRepository.findByInscripcion_Id(inscripcionId)
            .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<PagoResponse> listarPendientes(UserPrincipal principal) {
        return pagoRepository.findByInscripcion_Cliente_Usuario_Organizacion_IdAndEstadoDelPago(
            principal.getOrganizacionId(), EstadoDelPago.PENDIENTE
        ).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularIngresos(LocalDateTime desde, LocalDateTime hasta, UserPrincipal principal) {
        return pagoRepository.sumarIngresosPorOrganizacionEnPeriodo(principal.getOrganizacionId(), desde, hasta);
    }

    @Transactional(readOnly = true)
    public List<PagoResponse> obtenerPropios(UserPrincipal principal) {
        // Fix: get the client by userId first, then get their payments directly
        Cliente cliente = clienteRepository.findByUsuario_Id(principal.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("Perfil de cliente no encontrado"));
        return pagoRepository.findByInscripcion_Cliente_Id(cliente.getId())
            .stream().map(this::toResponse).toList();
    }

    private PagoResponse toResponse(Pago p) {
        return PagoResponse.builder()
            .id(p.getId())
            .monto(p.getMonto())
            .fechaDePago(p.getFechaDePago())
            .metodoDePago(p.getMetodoDePago())
            .estadoDelPago(p.getEstadoDelPago())
            .inscripcionId(p.getInscripcion().getId())
            .registradoPorId(p.getRegistradoPor().getId())
            .build();
    }
}