package edu.co.ustavillavicencio.coachboard.repository;

import edu.co.ustavillavicencio.coachboard.entity.Organizacion;
import edu.co.ustavillavicencio.coachboard.enums.PlanSaaS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizacionRepository extends JpaRepository<Organizacion, Long> {

    // Buscar organización por nombre
    Optional<Organizacion> findByNombre(String nombre);

    // Buscar organización por slug
    Optional<Organizacion> findBySlug(String slug);

    // Verificar si ya existe un nombre de organización
    boolean existsByNombre(String nombre);

    // Verificar si ya existe un slug
    boolean existsBySlug(String slug);

    // Obtener organizaciones activas o inactivas
    List<Organizacion> findByActiva(boolean activa);

    // Obtener organizaciones por tipo de plan
    List<Organizacion> findByPlan(PlanSaaS plan);

    // Contar organizaciones activas
    long countByActiva(boolean activa);

    // Contar organizaciones agrupadas por plan
    @Query("SELECT o.plan, COUNT(o) FROM Organizacion o " +
            "WHERE o.activa = true GROUP BY o.plan")
    List<Object[]> contarOrganizacionesPorPlan();
}