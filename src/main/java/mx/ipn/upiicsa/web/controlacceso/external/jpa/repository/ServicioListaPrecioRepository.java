package mx.ipn.upiicsa.web.controlacceso.external.jpa.repository;

import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.ServicioListaPrecio;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.pk.ServicioListaPrecioPK;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para gestionar la entidad {@link ServicioListaPrecio}.
 * Permite acceder a la tabla cruzada de precios por servicio y lista.
 *
 */
public interface ServicioListaPrecioRepository extends JpaRepository<ServicioListaPrecio, ServicioListaPrecioPK> {
}
