package edu.co.ustavillavicencio.coachboard.service;

import edu.co.ustavillavicencio.coachboard.dto.request.CrearInscripcionRequest;
import edu.co.ustavillavicencio.coachboard.dto.response.InscripcionResponse;
import edu.co.ustavillavicencio.coachboard.entity.Cliente;
import edu.co.ustavillavicencio.coachboard.entity.Inscripcion;
import edu.co.ustavillavicencio.coachboard.entity.Membresia;
import edu.co.ustavillavicencio.coachboard.entity.Usuario;
import edu.co.ustavillavicencio.coachboard.enums.EstadoInscripcion;
import edu.co.ustavillavicencio.coachboard.enums.Rol;
import edu.co.ustavillavicencio.coachboard.exception.BusinessException;
import edu.co.ustavillavicencio.coachboard.exception.ResourceNotFoundException;
import edu.co.ustavillavicencio.coachboard.repository.ClienteRepository;
import edu.co.ustavillavicencio.coachboard.repository.InscripcionRepository;
import edu.co.ustavillavicencio.coachboard.repository.UsuarioRepository;
import edu.co.ustavillavicencio.coachboard.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InscripcionService {

    private final InscripcionRepository inscripcionRepository;
    private final ClienteService clienteService;
    private final MembresiaService membresiaService;
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;

    @Transactional
    public InscripcionResponse crear(CrearInscripcionRequest req, UserPrincipal principal) {
        Cliente cliente = clienteService.findOrThrow(req.getClienteId());
        if (!cliente.getUsuario().getOrganizacion().getId().equals(principal.getOrganizacionId()))
            throw new BusinessException("El cliente no pertenece a su organización");

        Membresia membresia = membresiaService.findOrThrow(req.getTipoMembresiaId());
        if (!membresia.getOrganizacion().getId().equals(principal.getOrganizacionId()))
            throw new BusinessException("La membresía no pertenece a su organización");
        if (!membresia.isActivo())
            throw new BusinessException("La membresía no está activa");

        Usuario creadoPor = usuarioRepository.findById(principal.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        LocalDate fechaFin = req.getFechaDeInicio().plusDays(membresia.getDuracionDias());
        Inscripcion inscripcion = Inscripcion.builder()
            .fechaDeInicio(req.getFechaDeInicio())
            .fechaDeFin(fechaFin)
            .estado(EstadoInscripcion.PENDIENTE_PAGO)
            .cliente(cliente)
            .tipoDeMembresia(membresia)
            .creadoPor(creadoPor)
            .build();
        return toResponse(inscripcionRepository.save(inscripcion));
    }

    @Transactional(readOnly = true)
    public List<InscripcionResponse> listar(UserPrincipal principal) {
        // Use the org-scoped query without estado filter
        return inscripcionRepository.findByCliente_Usuario_Organizacion_Id(principal.getOrganizacionId())
            .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<InscripcionResponse> listarPorEstado(EstadoInscripcion estado, UserPrincipal principal) {
        return inscripcionRepository
            .findByCliente_Usuario_Organizacion_IdAndEstado(principal.getOrganizacionId(), estado)
            .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public InscripcionResponse obtener(Long id, UserPrincipal principal) {
        return toResponse(findInOrg(id, principal));
    }

    @Transactional(readOnly = true)
    public List<InscripcionResponse> historialCliente(Long clienteId, UserPrincipal principal) {
        Cliente cliente = clienteService.findOrThrow(clienteId);
        if (!cliente.getUsuario().getOrganizacion().getId().equals(principal.getOrganizacionId()))
            throw new BusinessException("El cliente no pertenece a su organización");
        return inscripcionRepository.findByCliente_IdOrderByFechaDeInicioDesc(clienteId)
            .stream().map(this::toResponse).toList();
    }

    @Transactional
    public InscripcionResponse cambiarEstado(Long id, EstadoInscripcion estado, UserPrincipal principal) {
        Inscripcion inscripcion = findInOrg(id, principal);
        inscripcion.setEstado(estado);
        return toResponse(inscripcionRepository.save(inscripcion));
    }

    @Transactional(readOnly = true)
    public List<InscripcionResponse> obtenerPropias(UserPrincipal principal) {
        // Fix: look up client by userId, not treat userId as clienteId
        Cliente cliente = clienteRepository.findByUsuario_Id(principal.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("Perfil de cliente no encontrado"));
        return inscripcionRepository.findByCliente_IdOrderByFechaDeInicioDesc(cliente.getId())
            .stream().map(this::toResponse).toList();
    }

    private Inscripcion findInOrg(Long id, UserPrincipal principal) {
        Inscripcion i = inscripcionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Inscripción no encontrada"));
        if (principal.getRol() == Rol.ORG_CLIENTE) {
            if (!i.getCliente().getUsuario().getId().equals(principal.getUserId()))
                throw new BusinessException("Acceso denegado");
        } else {
            if (!i.getCliente().getUsuario().getOrganizacion().getId().equals(principal.getOrganizacionId()))
                throw new BusinessException("La inscripción no pertenece a su organización");
        }
        return i;
    }

    public Inscripcion findOrThrow(Long id) {
        return inscripcionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Inscripción no encontrada: " + id));
    }

    public InscripcionResponse toResponse(Inscripcion i) {
        return InscripcionResponse.builder()
            .id(i.getId())
            .fechaDeInicio(i.getFechaDeInicio())
            .fechaDeFin(i.getFechaDeFin())  // Fix: was incorrectly i.getFechaDeInicio()
            .estado(i.getEstado())
            .clienteId(i.getCliente().getId())
            .nombreCliente(i.getCliente().getNombre())
            .tipoMembresiaId(i.getTipoDeMembresia().getId())
            .nombreMembresia(i.getTipoDeMembresia().getNombre())
            .precioMembresia(i.getTipoDeMembresia().getPrecio())
            .creadoPorId(i.getCreadoPor().getId())
            .build();
    }
}