package edu.co.ustavillavicencio.coachboard.controller;

import edu.co.ustavillavicencio.coachboard.dto.request.LoginRequest;
import edu.co.ustavillavicencio.coachboard.dto.request.RefreshTokenRequest;
import edu.co.ustavillavicencio.coachboard.dto.request.RegisterRequest;
import edu.co.ustavillavicencio.coachboard.dto.response.ApiResponse;
import edu.co.ustavillavicencio.coachboard.dto.response.JwtResponse;
import edu.co.ustavillavicencio.coachboard.entity.RefreshToken;
import edu.co.ustavillavicencio.coachboard.entity.Usuario;
import edu.co.ustavillavicencio.coachboard.exception.ResourceNotFoundException;
import edu.co.ustavillavicencio.coachboard.repository.UsuarioRepository;
import edu.co.ustavillavicencio.coachboard.security.JwtUtils;
import edu.co.ustavillavicencio.coachboard.security.RefreshTokenService;
import edu.co.ustavillavicencio.coachboard.security.UserPrincipal;
import edu.co.ustavillavicencio.coachboard.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<JwtResponse>> register(@Valid @RequestBody RegisterRequest req) {
        JwtResponse authResponse = usuarioService.register(req);
        Usuario usuario = usuarioRepository.findByUsername(req.getUsername())
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(usuario);
        JwtResponse response = JwtResponse.builder()
            .token(authResponse.getToken())
            .refreshToken(refreshToken.getToken())
            .userId(authResponse.getUserId())
            .username(authResponse.getUsername())
            .email(authResponse.getEmail())
            .rol(authResponse.getRol())
            .organizacionId(authResponse.getOrganizacionId())
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Registro exitoso", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@Valid @RequestBody LoginRequest req) {
        JwtResponse authResponse = usuarioService.login(req);
        Usuario usuario = usuarioRepository.findByUsername(req.getUsername())
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(usuario);
        JwtResponse response = JwtResponse.builder()
            .token(authResponse.getToken())
            .refreshToken(refreshToken.getToken())
            .userId(authResponse.getUserId())
            .username(authResponse.getUsername())
            .email(authResponse.getEmail())
            .rol(authResponse.getRol())
            .organizacionId(authResponse.getOrganizacionId())
            .build();
        return ResponseEntity.ok(ApiResponse.ok("Login exitoso", response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<JwtResponse>> refresh(@Valid @RequestBody RefreshTokenRequest req) {
        RefreshToken refreshToken = refreshTokenService.validateRefreshToken(req.getRefreshToken());
        Usuario usuario = refreshToken.getUsuario();
        UserPrincipal principal = UserPrincipal.build(usuario);
        String newAccessToken = jwtUtils.generateToken(principal);
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(usuario);
        JwtResponse response = JwtResponse.builder()
            .token(newAccessToken)
            .refreshToken(newRefreshToken.getToken())
            .userId(usuario.getId())
            .username(usuario.getUsername())
            .email(usuario.getEmail())
            .rol(usuario.getRol().name())
            .organizacionId(usuario.getOrganizacion() != null ? usuario.getOrganizacion().getId() : null)
            .build();
        return ResponseEntity.ok(ApiResponse.ok("Token renovado", response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@AuthenticationPrincipal UserPrincipal principal) {
        Usuario usuario = usuarioRepository.findById(principal.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        refreshTokenService.deleteByUsuario(usuario);
        return ResponseEntity.ok(ApiResponse.<Void>ok("Sesión cerrada", null));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<?>> perfil(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.ok(usuarioService.obtenerPerfil(principal)));
    }
}
