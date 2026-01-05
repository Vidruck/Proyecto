package mx.ipn.upiicsa.web.controlacceso.internal.bs.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tca01_persona")
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona")   // <--- Mapeo exacto a la BD
    private Integer id;

    @Column(name = "fk_id_genero") // <--- Mapeo exacto
    private Integer idGenero;

    @Column(name = "tx_nombre")
    private String nombre;

    @Column(name = "tx_primer_apellido")
    private String primerApellido;

    @Column(name = "tx_segundo_apellido")
    private String segundoApellido;

    @Column(name = "fh_nacimiento")
    private LocalDate fechaNacimiento;

    // Marcamos como Transient para evitar errores circulares por ahora,
    // o definimos la relación inversa si Usuario (Internal) también es Entity.
    // Dado que Usuario tiene @Table("tca02_usuario"), Hibernate podría confundirse
    // si no lo mapeamos explícitamente. Lo más seguro para romper el ciclo ahora es @Transient
    // o @OneToOne(mappedBy = "persona") si Usuario tiene la referencia.
    // Revisando tu código, Usuario NO tiene referencia directa a Persona en su versión internal,
    // así que lo mejor es @Transient para que Hibernate ignore este campo y no rompa.
    @Transient
    private Usuario usuario;
}
