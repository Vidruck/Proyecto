package mx.ipn.upiicsa.web.controlacceso.external.jpa.repository;

import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para gestionar la entidad {@link Cita}.
 * Incluye consultas para obtener el historial de citas por usuario o empleado.
 *
 */
@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {

    /**
     * Busca citas por ID de persona, ordenadas descendentemente por ID de cita.
     * Método heredado para compatibilidad.
     *
     * @param idPersona ID de la persona (cliente).
     * @return Lista de citas del cliente.
     */
    List<Cita> findByIdPersonaOrderByIdDesc(Integer idPersona);

    /**
     * Busca las citas asociadas a un ID (ya sea como cliente o como empleado).
     *
     * @param id ID del usuario (cliente o empleado).
     * @return Lista de citas donde el usuario participa.
     */
    @Query("SELECT c FROM Cita c WHERE c.idPersona = :id OR c.idEmpleado = :id ORDER BY c.id DESC")
    List<Cita> findMisCitas(@Param("id") Integer id);
}