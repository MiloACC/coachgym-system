package edu.co.ustavillavicencio.coachboard.service;

import edu.co.ustavillavicencio.coachboard.dto.request.ActualizarMembresiaRequest;
import edu.co.ustavillavicencio.coachboard.dto.request.CrearMembresiaRequest;
import edu.co.ustavillavicencio.coachboard.dto.response.MembresiaResponse;
import edu.co.ustavillavicencio.coachboard.entity.Membresia;
import edu.co.ustavillavicencio.coachboard.entity.Organizacion;
import edu.co.ustavillavicencio.coachboard.exception.BusinessException;
import edu.co.ustavillavicencio.coachboard.exception.ResourceNotFoundException;
import edu.co.ustavillavicencio.coachboard.repository.MembresiaRepository;
import edu.co.ustavillavicencio.coachboard.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MembresiaService {

    private final MembresiaRepository membresiaRepository;
    private final OrganizacionService organizacionService;

    @Transactional
    public MembresiaResponse crear(CrearMembresiaRequest req, UserPrincipal principal) {
        Organizacion org = organizacionService.findOrThrow(principal.getOrganizacionId());
        if (membresiaRepository.existsByNombreAndOrganizacion_Id(req.getNombre(), org.getId()))
            throw new BusinessException("Ya existe una membresÃ­a con ese nombre en la organizaciÃ³n");

        Membresia m = Membresia.builder()
            .nombre(req.getNombre())
            .descripcion(req.getDescripcion())
            .duracionDias(req.getDuracionDias())
            .precio(req.getPrecio())
            .organizacion(org)
            .build();
        return toResponse(membresiaRepository.save(m));
    }

    @Transactional(readOnly = true)
    public List<MembresiaResponse> listar(UserPrincipal principal) {
        return membresiaRepository.findByOrganizacion_Id(principal.getOrganizacionId())
            .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<MembresiaResponse> listarActivas(UserPrincipal principal) {
        return membresiaRepository.findByOrganizacion_IdAndActivo(principal.getOrganizacionId(), true)
            .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public MembresiaResponse obtener(Long id, UserPrincipal principal) {
        return toResponse(findInOrg(id, principal.getOrganizacionId()));
    }

    @Transactional
    public MembresiaResponse actualizar(Long id, ActualizarMembresiaRequest req, UserPrincipal principal) {
        Membresia m = findInOrg(id, principal.getOrganizacionId());
        if (!m.getNombre().equals(req.getNombre()) &&
                membresiaRepository.existsByNombreAndOrganizacion_Id(req.getNombre(), principal.getOrganizacionId()))
            throw new BusinessException("Ya existe una membresÃ­a con ese nombre");
        m.setNombre(req.getNombre());
        m.setDescripcion(req.getDescripcion());
        m.setDuracionDias(req.getDuracionDias());
        m.setPrecio(req.getPrecio());
        return toResponse(membresiaRepository.save(m));
    }

    @Transactional
    public MembresiaResponse toggleEstado(Long id, UserPrincipal principal) {
        Membresia m = findInOrg(id, principal.getOrganizacionId());
        m.setActivo(!m.isActivo());
        return toResponse(membresiaRepository.save(m));
    }

    private Membresia findInOrg(Long id, Long orgId) {
        return membresiaRepository.findByIdAndOrganizacion_Id(id, orgId)
            .orElseThrow(() -> new ResourceNotFoundException("MembresÃ­a no encontrada"));
    }

    public Membresia findOrThrow(Long id) {
        return membresiaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("MembresÃ­a no encontrada: " + id));
    }

    private MembresiaResponse toResponse(Membresia m) {
        return MembresiaResponse.builder()
            .id(m.getId())
            .nombre(m.getNombre())
            .descripcion(m.getDescripcion())
            .duracionDias(m.getDuracionDias())
            .precio(m.getPrecio())
            .activo(m.isActivo())
            .organizacionId(m.getOrganizacion().getId())
            .build();
    }
}
