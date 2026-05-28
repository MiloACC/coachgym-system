package edu.co.ustavillavicencio.coachboard.service;

import edu.co.ustavillavicencio.coachboard.dto.request.ActualizarOrganizacionRequest;
import edu.co.ustavillavicencio.coachboard.dto.request.CrearOrganizacionRequest;
import edu.co.ustavillavicencio.coachboard.dto.response.OrganizacionResponse;
import edu.co.ustavillavicencio.coachboard.entity.Organizacion;
import edu.co.ustavillavicencio.coachboard.exception.BusinessException;
import edu.co.ustavillavicencio.coachboard.exception.ResourceNotFoundException;
import edu.co.ustavillavicencio.coachboard.repository.OrganizacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizacionService {

    private final OrganizacionRepository organizacionRepository;

    @Transactional
    public OrganizacionResponse crear(CrearOrganizacionRequest req) {
        if (organizacionRepository.existsByNombre(req.getNombre()))
            throw new BusinessException("Ya existe una organización con ese nombre");
        if (organizacionRepository.existsBySlug(req.getSlug()))
            throw new BusinessException("El slug ya está en uso");

        Organizacion org = Organizacion.builder()
            .nombre(req.getNombre())
            .slug(req.getSlug())
            .tipoDeporte(req.getTipoDeporte())
            .plan(req.getPlan())
            .build();
        return toResponse(organizacionRepository.save(org));
    }

    @Transactional(readOnly = true)
    public List<OrganizacionResponse> listar() {
        return organizacionRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<OrganizacionResponse> listarActivas() {
        return organizacionRepository.findByActiva(true).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public OrganizacionResponse obtener(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Transactional
    public OrganizacionResponse actualizar(Long id, ActualizarOrganizacionRequest req) {
        Organizacion org = findOrThrow(id);
        if (!org.getNombre().equals(req.getNombre()) && organizacionRepository.existsByNombre(req.getNombre()))
            throw new BusinessException("Ya existe una organización con ese nombre");
        org.setNombre(req.getNombre());
        org.setTipoDeporte(req.getTipoDeporte());
        org.setPlan(req.getPlan());
        return toResponse(organizacionRepository.save(org));
    }

    @Transactional
    public OrganizacionResponse toggleEstado(Long id) {
        Organizacion org = findOrThrow(id);
        org.setActiva(!org.isActiva());
        return toResponse(organizacionRepository.save(org));
    }

    public Organizacion findOrThrow(Long id) {
        return organizacionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Organización no encontrada: " + id));
    }

    private OrganizacionResponse toResponse(Organizacion o) {
        return OrganizacionResponse.builder()
            .id(o.getId())
            .nombre(o.getNombre())
            .slug(o.getSlug())
            .tipoDeporte(o.getTipoDeporte())
            .plan(o.getPlan())
            .activa(o.isActiva())
            .fechaDeCreacion(o.getFechaDeCreacion())
            .build();
    }
}