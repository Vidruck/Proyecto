package mx.ipn.upiicsa.web.controlacceso.external.jpa.model;

import jakarta.persistence.*;
import lombok.*;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Persona;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "tca01_persona")
public class PersonaJpa {
    @Id
    @SequenceGenerator(name = "tca01_persona_id_persona_seq", sequenceName = "tca01_persona_id_persona_seq", allocationSize = 1)
    @GeneratedValue(generator = "tca01_persona_id_persona_seq", strategy = GenerationType.SEQUENCE)
    @Column(name = "id_persona")
    private Integer id;
    @Column(name = "fk_id_genero")
    private Integer idGenero;
    @Column(name = "tx_nombre")
    private String nombre;
    @Column(name = "tx_primer_apellido")
    private String primerApellido;
    @Column(name = "tx_segundo_apellido")
    private String segundoApellido;
    @Column(name = "fh_nacimiento")
    private LocalDate fechaNacimiento;
    @ManyToOne
    @JoinColumn(name = "fk_id_genero", referencedColumnName = "id_genero", insertable = false, updatable = false)
    private GeneroJpa genero;
    @OneToOne(mappedBy = "persona")
    private UsuarioJpa usuario;

    public Persona toEntity() {
        return Persona.builder()
                .id(this.id)
                .idGenero(this.idGenero)
                .nombre(this.nombre)
                .primerApellido(this.primerApellido)
                .segundoApellido(this.segundoApellido)
                .build();
    }

    public static PersonaJpa fromEntity(Persona persona) {
        return PersonaJpa.builder()
                .id(persona.getId())
                .idGenero(persona.getIdGenero())
                .nombre(persona.getNombre())
                .primerApellido(persona.getPrimerApellido())
                .segundoApellido(persona.getSegundoApellido())
                .fechaNacimiento(persona.getFechaNacimiento())
                .build();
    }
}
