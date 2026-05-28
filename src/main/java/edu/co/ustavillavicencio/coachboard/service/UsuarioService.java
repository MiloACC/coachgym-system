package edu.co.ustavillavicencio.coachboard.service;

import edu.co.ustavillavicencio.coachboard.dto.request.CrearAdminOrganizacionRequest;
import edu.co.ustavillavicencio.coachboard.dto.request.CrearOrganizacionRequest;
import edu.co.ustavillavicencio.coachboard.dto.request.CrearStaffRequest;
import edu.co.ustavillavicencio.coachboard.dto.request.LoginRequest;
import edu.co.ustavillavicencio.coachboard.dto.request.RegisterRequest;
import edu.co.ustavillavicencio.coachboard.dto.response.JwtResponse;
import edu.co.ustavillavicencio.coachboard.dto.response.OrganizacionResponse;
import edu.co.ustavillavicencio.coachboard.dto.response.UsuarioResponse;
import edu.co.ustavillavicencio.coachboard.entity.Organizacion;
import edu.co.ustavillavicencio.coachboard.entity.Usuario;
import edu.co.ustavillavicencio.coachboard.enums.Rol;
import edu.co.ustavillavicencio.coachboard.exception.BusinessException;
import edu.co.ustavillavicencio.coachboard.exception.ResourceNotFoundException;
import edu.co.ustavillavicencio.coachboard.repository.UsuarioRepository;
import edu.co.ustavillavicencio.coachboard.security.JwtUtils;
import edu.co.ustavillavicencio.coachboard.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final OrganizacionService organizacionService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Transactional
    public JwtResponse register(RegisterRequest req) {
        CrearOrganizacionRequest orgReq = new CrearOrganizacionRequest();
        orgReq.setNombre(req.getNombreOrganizacion());
        orgReq.setSlug(req.getSlug());
        orgReq.setTipoDeporte(req.getTipoDeporte());
        orgReq.setPlan(req.getPlan());
        OrganizacionResponse orgResp = organizacionService.crear(orgReq);

        Organizacion org = organizacionService.findOrThrow(orgResp.getId());

        validarUnicidad(req.getUsername(), req.getEmail());
        Usuario usuario = Usuario.builder()
            .username(req.getUsername())
            .password(passwordEncoder.encode(req.getPassword()))
            .email(req.getEmail())
            .rol(Rol.ORG_ADMIN)
            .organizacion(org)
            .build();
        usuario = usuarioRepository.save(usuario);

        UserPrincipal principal = UserPrincipal.build(usuario);
        String token = jwtUtils.generateToken(principal);
        return JwtResponse.builder()
            .token(token)
            .userId(principal.getUserId())
            .username(principal.getUsername())
            .email(principal.getEmail())
            .rol(principal.getRol().name())
            .organizacionId(principal.getOrganizacionId())
            .build();
    }

    public JwtResponse login(LoginRequest req) {
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        String token = jwtUtils.generateToken(principal);
        return JwtResponse.builder()
            .token(token)
            .userId(principal.getUserId())
            .username(principal.getUsername())
            .email(principal.getEmail())
            .rol(principal.getRol().name())
            .organizacionId(principal.getOrganizacionId())
            .build();
    }

    @Transactional
    public UsuarioResponse crearAdminOrganizacion(Long orgId, CrearAdminOrganizacionRequest req) {
        Organizacion org = organizacionService.findOrThrow(orgId);
        validarUnicidad(req.getUsername(), req.getEmail());
        Usuario usuario = Usuario.builder()
            .username(req.getUsername())
            .password(passwordEncoder.encode(req.getPassword()))
            .email(req.getEmail())
            .rol(Rol.ORG_ADMIN)
            .organizacion(org)
            .build();
        return toResponse(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioResponse crearStaff(CrearStaffRequest req, UserPrincipal principal) {
        Organizacion org = organizacionService.findOrThrow(principal.getOrganizacionId());
        validarUnicidad(req.getUsername(), req.getEmail());
        Usuario usuario = Usuario.builder()
            .username(req.getUsername())
            .password(passwordEncoder.encode(req.getPassword()))
            .email(req.getEmail())
            .rol(Rol.ORG_STAFF)
            .organizacion(org)
            .build();
        return toResponse(usuarioRepository.save(usuario));
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarStaff(UserPrincipal principal) {
        return usuarioRepository.findByOrganizacion_IdAndRol(principal.getOrganizacionId(), Rol.ORG_STAFF)
            .stream().map(this::toResponse).toList();
    }

    @Transactional
    public UsuarioResponse toggleEstado(Long id, UserPrincipal principal) {
        Usuario usuario = usuarioRepository.findByIdAndOrganizacion_Id(id, principal.getOrganizacionId())
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        usuario.setActivo(!usuario.isActivo());
        return toResponse(usuarioRepository.save(usuario));
    }

    @Transactional(readOnly = true)
    public UsuarioResponse obtenerPerfil(UserPrincipal principal) {
        return usuarioRepository.findById(principal.getUserId())
            .map(this::toResponse)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    private void validarUnicidad(String username, String email) {
        if (usuarioRepository.existsByUsername(username))
            throw new BusinessException("El username ya está en uso");
        if (usuarioRepository.existsByEmail(email))
            throw new BusinessException("El email ya está en uso");
    }

    public Usuario crearUsuarioCliente(String username, String password, String email, Organizacion org) {
        validarUnicidad(username, email);
        return Usuario.builder()
            .username(username)
            .password(passwordEncoder.encode(password))
            .email(email)
            .rol(Rol.ORG_CLIENTE)
            .organizacion(org)
            .build();
    }

    public UsuarioResponse toResponse(Usuario u) {
        return UsuarioResponse.builder()
            .id(u.getId())
            .username(u.getUsername())
            .email(u.getEmail())
            .rol(u.getRol().name())
            .activo(u.isActivo())
            .fechaDeCreacion(u.getFechaDeCreacion())
            .organizacionId(u.getOrganizacion() != null ? u.getOrganizacion().getId() : null)
            .nombreOrganizacion(u.getOrganizacion() != null ? u.getOrganizacion().getNombre() : null)
            .build();
    }
}