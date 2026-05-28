package edu.co.ustavillavicencio.coachboard.entity;

import edu.co.ustavillavicencio.coachboard.enums.Rol;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre de usuario único
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    // Contraseña encriptada
    @Column(nullable = false)
    private String password;

    // Correo electrónico del usuario
    @Column(nullable = false, unique = true)
    private String email;

    // Rol del usuario en el sistema
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    // Organización a la que pertenece el usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacion_id", nullable = true)
    private Organizacion organizacion;

    // Estado de la cuenta
    @Column(nullable = false)
    private boolean activo;

    // Fecha de creación de la cuenta
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaDeCreacion;

    // Inicializar valores automáticos
    @PrePersist
    public void prePersist() {
        this.fechaDeCreacion = LocalDateTime.now();
        this.activo = true;
    }
}