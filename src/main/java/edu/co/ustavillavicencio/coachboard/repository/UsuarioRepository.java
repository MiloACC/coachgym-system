package edu.co.ustavillavicencio.coachboard.repository;

import edu.co.ustavillavicencio.coachboard.entity.Usuario;
import edu.co.ustavillavicencio.coachboard.enums.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Buscar usuario por username
    Optional<Usuario> findByUsername(String username);

    // Buscar usuario por email
    Optional<Usuario> findByEmail(String email);

    // Verificar si ya existe un username
    boolean existsByUsername(String username);

    // Verificar si ya existe un email
    boolean existsByEmail(String email);

    // Contar usuarios por rol dentro de una organización
    long countByOrganizacion_IdAndRol(Long organizacionId, Rol rol);

    // Obtener usuarios por rol dentro de una organización
    List<Usuario> findByOrganizacion_IdAndRol(Long organizacionId, Rol rol);

    // Obtener usuarios activos o inactivos de una organización
    List<Usuario> findByOrganizacion_IdAndActivo(Long organizacionId,
                                                 boolean activo);

    // Buscar usuario por id validando organización
    Optional<Usuario> findByIdAndOrganizacion_Id(Long id, Long organizacionId);
}