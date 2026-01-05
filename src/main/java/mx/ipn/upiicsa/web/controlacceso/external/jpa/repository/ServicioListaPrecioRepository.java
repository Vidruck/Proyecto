package mx.ipn.upiicsa.web.controlacceso.external.jpa.repository;

import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.ServicioListaPrecio;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.pk.ServicioListaPrecioPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicioListaPrecioRepository extends JpaRepository<ServicioListaPrecio, ServicioListaPrecioPK> {
}
