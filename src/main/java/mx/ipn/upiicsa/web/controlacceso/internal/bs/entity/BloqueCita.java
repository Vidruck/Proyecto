package mx.ipn.upiicsa.web.controlacceso.internal.bs.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * Entidad que representa un bloque de horario ocupado por una cita.
 * Mantiene la relación entre la cita, la sucursal y el rango de tiempo.
 */
@Entity
@Table(name = "tce07_bloque_cita")
public class BloqueCita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bloque")
    private Integer id;

    @Column(name = "fk_id_sucursal")
    private Integer idSucursal;

    @Column(name = "fk_id_cita")
    private Integer idCita; // Aquí guardaremos el ID de la Cita creada

    @Column(name = "fh_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "fh_fin")
    private LocalDateTime fechaFin;
}
