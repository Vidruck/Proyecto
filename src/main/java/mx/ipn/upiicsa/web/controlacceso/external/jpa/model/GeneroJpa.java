package mx.ipn.upiicsa.web.controlacceso.external.jpa.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "cca01_genero")
public class GeneroJpa {
    @Id
    @Column(name = "id_genero")
    private Integer id;
    @Column(name = "tx_nombre")
    private String nombre;
    @Column(name = "tx_descripcion")
    private String descripcion;
    @Column(name = "st_activo")
    private Boolean activo;
    @OneToMany(mappedBy = "genero")
    List<PersonaJpa> personas;
}
