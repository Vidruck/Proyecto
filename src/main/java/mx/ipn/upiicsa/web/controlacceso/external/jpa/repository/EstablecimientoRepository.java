package mx.ipn.upiicsa.web.controlacceso.external.jpa.repository;

import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Establecimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para gestionar la entidad {@link Establecimiento}.
 * Provee operaciones CRUD estándar para los negocios registrados.
 *
 */
@Repository
public interface EstablecimientoRepository extends JpaRepository<Establecimiento, Integer> {
}
