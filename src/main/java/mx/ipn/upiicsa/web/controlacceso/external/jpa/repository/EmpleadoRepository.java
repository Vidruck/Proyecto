package mx.ipn.upiicsa.web.controlacceso.external.jpa.repository;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Empleado;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {
    List<Empleado> findBySucursalId(Integer idSucursal);
}
