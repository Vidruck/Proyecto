package mx.ipn.upiicsa.web.controlacceso.external.jpa.repository;

import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repositorio JPA para gestionar la entidad {@link Servicio}.
 * Permite filtrar servicios por su estado de actividad.
 *
 */
public interface ServicioRepository extends JpaRepository<Servicio, Integer> {

    /**
     * Encuentra servicios basándose en su estado de activo/inactivo.
     *
     * @param activo 1 para activo, 0 para inactivo.
     * @return Lista de servicios que coinciden con el estado.
     */
    List<Servicio> findByActivo(Integer activo);
}
