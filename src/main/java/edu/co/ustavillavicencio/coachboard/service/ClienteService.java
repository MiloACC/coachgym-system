package edu.co.ustavillavicencio.coachboard.service;

import edu.co.ustavillavicencio.coachboard.dto.request.ActualizarClienteRequest;
import edu.co.ustavillavicencio.coachboard.dto.request.CrearClienteRequest;
import edu.co.ustavillavicencio.coachboard.dto.response.ClienteResponse;
import edu.co.ustavillavicencio.coachboard.entity.Cliente;
import edu.co.ustavillavicencio.coachboard.entity.Organizacion;
import edu.co.ustavillavicencio.coachboard.entity.Usuario;
import edu.co.ustavillavicencio.coachboard.exception.BusinessException;
import edu.co.ustavillavicencio.coachboard.exception.ResourceNotFoundException;
import edu.co.ustavillavicencio.coachboard.repository.ClienteRepository;
import edu.co.ustavillavicencio.coachboard.repository.UsuarioRepository;
import edu.co.ustavillavicencio.coachboard.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final OrganizacionService organizacionService;
    private final UsuarioService usuarioService;

    @Transactional
    public ClienteResponse registrar(CrearClienteRequest req, UserPrincipal principal) {
        if (clienteRepository.existsByDocumento(req.getDocumento()))
            throw new BusinessException("Ya existe un cliente con ese documento");

        Organizacion org = organizacionService.findOrThrow(principal.getOrganizacionId());
        Usuario usuario = usuarioRepository.save(
            usuarioService.crearUsuarioCliente(req.getUsername(), req.getPassword(), req.getEmail(), org)
        );

        Cliente cliente = Cliente.builder()
            .nombre(req.getNombre())
            .celular(req.getCelular())
            .documento(req.getDocumento())
            .usuario(usuario)
            .build();
        return toResponse(clienteRepository.save(cliente));
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> listar(UserPrincipal principal) {
        return clienteRepository.findByUsuario_Organizacion_Id(principal.getOrganizacionId())
            .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponse obtener(Long id, UserPrincipal principal) {
        return toResponse(findInOrg(id, principal.getOrganizacionId()));
    }

    @Transactional
    public ClienteResponse actualizar(Long id, ActualizarClienteRequest req, UserPrincipal principal) {
        Cliente cliente = findInOrg(id, principal.getOrganizacionId());
        cliente.setNombre(req.getNombre());
        cliente.setCelular(req.getCelular());
        return toResponse(clienteRepository.save(cliente));
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> buscar(String nombre, UserPrincipal principal) {
        return clienteRepository.buscarPorNombreEnOrganizacion(nombre, principal.getOrganizacionId())
            .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponse obtenerPropio(UserPrincipal principal) {
        return clienteRepository.findByUsuario_Id(principal.getUserId())
            .map(this::toResponse)
            .orElseThrow(() -> new ResourceNotFoundException("Perfil de cliente no encontrado"));
    }

    private Cliente findInOrg(Long id, Long orgId) {
        return clienteRepository.findByIdAndUsuario_Organizacion_Id(id, orgId)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
    }

    public Cliente findOrThrow(Long id) {
        return clienteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado: " + id));
    }

    public ClienteResponse toResponse(Cliente c) {
        return ClienteResponse.builder()
            .id(c.getId())
            .nombre(c.getNombre())
            .celular(c.getCelular())
            .documento(c.getDocumento())
            .fechaDeRegistro(c.getFechaDeRegistro())
            .usuarioId(c.getUsuario().getId())
            .email(c.getUsuario().getEmail())
            .build();
    }
}