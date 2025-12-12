package mx.ipn.upiicsa.web.controlacceso.external.jpa.repository;

import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ServicioRepository extends JpaRepository<Servicio, Integer> {
    List<Servicio> findByActivo(Integer activo);
}
