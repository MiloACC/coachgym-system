package edu.co.ustavillavicencio.coachboard.config;

import edu.co.ustavillavicencio.coachboard.entity.Usuario;
import edu.co.ustavillavicencio.coachboard.enums.Rol;
import edu.co.ustavillavicencio.coachboard.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.username:platform_admin}")
    private String adminUsername;

    @Value("${admin.email:admin@coachboard.com}")
    private String adminEmail;

    @Value("${admin.password:Admin@1234!}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (!usuarioRepository.existsByUsername(adminUsername)) {
            Usuario admin = Usuario.builder()
                .username(adminUsername)
                .email(adminEmail)
                .password(passwordEncoder.encode(adminPassword))
                .rol(Rol.PLATFORM_ADMIN)
                .activo(true)
                .build();
            usuarioRepository.save(admin);
            log.info("PLATFORM_ADMIN creado: {}", adminUsername);
        }
    }
}
