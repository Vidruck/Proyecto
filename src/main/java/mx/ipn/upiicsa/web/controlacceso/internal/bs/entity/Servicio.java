package mx.ipn.upiicsa.web.controlacceso.internal.bs.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cci01_servicio")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_servicio")
    private Integer id;

    @Column(name = "tx_nombre")
    private String nombre;

    @Column(name = "tx_descripcion")
    private String descripcion;

    @Column(name = "nu_duracion")
    private Integer duracion; // En min.

    @Column(name = "st_activo")
    private Integer activo;
}