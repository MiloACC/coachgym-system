package edu.co.ustavillavicencio.coachboard.repository;

import edu.co.ustavillavicencio.coachboard.entity.Membresia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MembresiaRepository extends JpaRepository<Membresia, Long> {

    // Obtener todas las membresías de una organización
    List<Membresia> findByOrganizacion_Id(Long organizacionId);

    // Obtener membresías activas de una organización
    List<Membresia> findByOrganizacion_IdAndActivo(
            Long organizacionId, boolean activo);

    // Buscar membresía por id validando organización
    Optional<Membresia> findByIdAndOrganizacion_Id(
            Long id, Long organizacionId);

    // Verificar si ya existe una membresía con ese nombre
    boolean existsByNombreAndOrganizacion_Id(
            String nombre, Long organizacionId);

    // Contar membresías activas de una organización
    long countByOrganizacion_IdAndActivo(Long organizacionId, boolean activo);
}