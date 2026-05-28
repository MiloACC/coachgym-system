package edu.co.ustavillavicencio.coachboard.repository;

import edu.co.ustavillavicencio.coachboard.entity.Pago;
import edu.co.ustavillavicencio.coachboard.enums.EstadoDelPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    // Obtener pagos de una inscripción
    List<Pago> findByInscripcion_Id(Long inscripcionId);

    // Obtener pagos completados en un rango de fechas
    @Query("SELECT p FROM Pago p " +
            "WHERE p.inscripcion.cliente.usuario.organizacion.id = :organizacionId " +
            "AND p.estadoDelPago = 'COMPLETADO' " +
            "AND p.fechaDePago BETWEEN :desde AND :hasta " +
            "ORDER BY p.fechaDePago DESC")
    List<Pago> findCompletadosPorOrganizacionEnPeriodo(
            @Param("organizacionId") Long organizacionId,
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta);

    // Calcular ingresos totales en un período
    @Query("SELECT COALESCE(SUM(p.monto), 0) FROM Pago p " +
            "WHERE p.inscripcion.cliente.usuario.organizacion.id = :organizacionId " +
            "AND p.estadoDelPago = 'COMPLETADO' " +
            "AND p.fechaDePago BETWEEN :desde AND :hasta")
    BigDecimal sumarIngresosPorOrganizacionEnPeriodo(
            @Param("organizacionId") Long organizacionId,
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta);

    // Contar pagos agrupados por método de pago
    @Query("SELECT p.metodoDePago, COUNT(p) FROM Pago p " +
            "WHERE p.inscripcion.cliente.usuario.organizacion.id = :organizacionId " +
            "AND p.estadoDelPago = 'COMPLETADO' " +
            "GROUP BY p.metodoDePago")
    List<Object[]> contarPagosPorMetodoEnOrganizacion(
            @Param("organizacionId") Long organizacionId);

    // Obtener pagos pendientes de una organización
    List<Pago> findByInscripcion_Cliente_Usuario_Organizacion_IdAndEstadoDelPago(
            Long organizacionId, EstadoDelPago estado);
}