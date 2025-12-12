package mx.ipn.upiicsa.web.controlacceso.internal.bs.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tci05_cita" )
public class Cita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita")
    private Integer id;
    @Column(name = "fk_id_persona")
    private Integer idPersona;

    @Column(name = "fk_id_servicio")
    private Integer idServicio;

    @Column(name = "fk_id_sucursal")
    private Integer idSucursal;

    @Column(name = "fk_id_empleado")
    private Integer idEmpleado;

    @Column(name = "fk_id_lista_precio")
    private Integer idListaPrecio;
}
