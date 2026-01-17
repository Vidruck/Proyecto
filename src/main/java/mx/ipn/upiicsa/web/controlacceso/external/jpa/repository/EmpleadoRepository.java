package mx.ipn.upiicsa.web.controlacceso.external.jpa.repository;

import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Empleado;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para gestionar la entidad {@link Empleado}.
 * Permite filtrar empleados por sucursal.
 *
 */
@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {
    /**
     * Encuentra todos los empleados asignados a una sucursal específica.
     *
     * @param idSucursal ID de la sucursal.
     * @return Lista de empleados de la sucursal.
     */
    List<Empleado> findBySucursalId(Integer idSucursal);
}
