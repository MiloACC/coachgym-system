package edu.co.ustavillavicencio.coachboard.repository;

import edu.co.ustavillavicencio.coachboard.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Buscar cliente por documento
    Optional<Cliente> findByDocumento(String documento);

    // Buscar cliente por número celular
    Optional<Cliente> findByCelular(String celular);

    // Buscar cliente por id validando organización
    Optional<Cliente> findByIdAndUsuario_Organizacion_Id(
            Long id, Long organizacionId);

    // Obtener todos los clientes de una organización
    List<Cliente> findByUsuario_Organizacion_Id(Long organizacionId);

    // Contar clientes registrados en una organización
    long countByUsuario_Organizacion_Id(Long organizacionId);

    // Buscar clientes por nombre dentro de una organización
    @Query("SELECT c FROM Cliente c " +
            "WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) " +
            "AND c.usuario.organizacion.id = :organizacionId")
    List<Cliente> buscarPorNombreEnOrganizacion(
            @Param("nombre") String nombre,
            @Param("organizacionId") Long organizacionId);

    // Verificar si ya existe un documento registrado
    boolean existsByDocumento(String documento);

    // Buscar cliente asociado a un usuario
    Optional<Cliente> findByUsuario_Id(Long usuarioId);
}