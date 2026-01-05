package mx.ipn.upiicsa.web.controlacceso.external.jpa.repository;

import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Cita;
import mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.projection.ReporteDato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ReporteRepository extends JpaRepository<Cita,Integer> {
    //1. Ganancias por sucursal
    //Se suma el precio (tci02) de cada cita (tci05) agrupado por sucursal (tce02)
    @Query(value = """
        SELECT s.tx_nombre as etiqueta, SUM(slp.nu_precio) as valor
        FROM tci05_cita c
        JOIN tce02_sucursal s ON c.fk_id_sucursal = s.id_sucursal
        JOIN tci02_servicio_lista_precio slp 
            ON c.fk_id_servicio = slp.fk_id_servicio 
            AND c.fk_id_lista_precio = slp.fk_id_lista_precio
        GROUP BY s.tx_nombre
    """, nativeQuery = true)
    List<ReporteDato> obtenerGananciasPorSucursal();

    // 2.(Top 5) Servicios Más Solicitados
    // Contamos cuántas veces aparece cada id_servicio
    @Query(value = """
        SELECT ser.tx_nombre as etiqueta, COUNT(c.id_cita) as valor
        FROM tci05_cita c
        JOIN cci01_servicio ser ON c.fk_id_servicio = ser.id_servicio
        GROUP BY ser.tx_nombre
        ORDER BY valor DESC
        LIMIT 5
    """, nativeQuery = true)
    List<ReporteDato> obtenerTopServicios();
}
