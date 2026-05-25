package edu.co.ustavillavicencio.coachboard.repository;

import edu.co.ustavillavicencio.coachboard.entity.Notificacion;
import edu.co.ustavillavicencio.coachboard.enums.TipoDeNotificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    // Obtener notificaciones de un cliente ordenadas por fecha
    List<Notificacion> findByCliente_IdOrderByFechaGeneracionDesc(Long clienteId);

    // Obtener notificaciones no leídas de un cliente
    List<Notificacion> findByCliente_IdAndLeida(Long clienteId, boolean leida);

    // Contar notificaciones no leídas de un cliente
    long countByCliente_IdAndLeida(Long clienteId, boolean leida);

    // Verificar si ya existe una notificación para una inscripción
    boolean existsByInscripcion_IdAndTipo(Long inscripcionId,
                                          TipoDeNotificacion tipo);

    // Marcar todas las notificaciones de un cliente como leídas
    @Modifying
    @Query("UPDATE Notificacion n SET n.leida = true " +
            "WHERE n.cliente.id = :clienteId AND n.leida = false")
    void marcarTodasComoLeidas(@Param("clienteId") Long clienteId);

    // Obtener notificaciones no leídas de una organización
    @Query("SELECT n FROM Notificacion n " +
            "WHERE n.cliente.usuario.organizacion.id = :organizacionId " +
            "AND n.leida = false " +
            "ORDER BY n.fechaGeneracion DESC")
    List<Notificacion> findNoLeidasPorOrganizacion(
            @Param("organizacionId") Long organizacionId);
}