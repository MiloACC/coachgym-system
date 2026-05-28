package edu.co.ustavillavicencio.coachboard.repository;

import edu.co.ustavillavicencio.coachboard.entity.Inscripcion;
import edu.co.ustavillavicencio.coachboard.enums.EstadoInscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    // Buscar inscripción activa de un cliente
    Optional<Inscripcion> findByCliente_IdAndEstado(
            Long clienteId, EstadoInscripcion estado);

    // Obtener historial de inscripciones del cliente
    List<Inscripcion> findByCliente_IdOrderByFechaDeInicioDesc(Long clienteId);

    // Obtener todas las inscripciones de una organización
    List<Inscripcion> findByCliente_Usuario_Organizacion_Id(Long organizacionId);

    // Obtener inscripciones por estado dentro de una organización
    List<Inscripcion> findByCliente_Usuario_Organizacion_IdAndEstado(
            Long organizacionId, EstadoInscripcion estado);

    // Buscar inscripciones activas que vencen en una fecha específica
    @Query("SELECT i FROM Inscripcion i " +
            "WHERE i.fechaDeFin = :fecha " +
            "AND i.estado = 'ACTIVA' " +
            "AND i.cliente.usuario.organizacion.id = :organizacionId")
    List<Inscripcion> findActivasQueVencenEn(
            @Param("fecha") LocalDate fecha,
            @Param("organizacionId") Long organizacionId);

    // Buscar inscripciones activas ya vencidas
    @Query("SELECT i FROM Inscripcion i " +
            "WHERE i.fechaDeFin < :hoy " +
            "AND i.estado = 'ACTIVA'")
    List<Inscripcion> findActivasVencidas(@Param("hoy") LocalDate hoy);

    // Contar inscripciones activas de una organización
    long countByCliente_Usuario_Organizacion_IdAndEstado(
            Long organizacionId, EstadoInscripcion estado);
}