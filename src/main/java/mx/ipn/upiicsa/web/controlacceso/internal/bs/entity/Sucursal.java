package mx.ipn.upiicsa.web.controlacceso.internal.bs.entity;

import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tce02_sucursal")
public class Sucursal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sucursal")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "fk_id_establecimiento")
    private Establecimiento establecimiento;

    @Column(name = "tx_nombre")
    private String nombre;

    // Columna espacial PostGIS (SRID 4326 es el estándar GPS: Lat/Lon)
    @Column(name = "gm_ubicacion", columnDefinition = "geometry(Point,4326)")
    private Point ubicacion;
}