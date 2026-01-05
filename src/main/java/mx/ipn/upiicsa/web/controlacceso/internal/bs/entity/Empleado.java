package mx.ipn.upiicsa.web.controlacceso.internal.bs.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tce03_empleado")
public class Empleado {

    @Id
    @Column(name = "id_empleado")
    private Integer id;

    // Se usa cascade = CascadeType.ALL
    // Esto significa: "Si guardo al Empleado, guarda también a la Persona automáticamente"
    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "id_empleado")
    private Persona persona;

    @ManyToOne
    @JoinColumn(name = "fk_id_sucursal")
    private Sucursal sucursal;
}
