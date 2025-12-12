package mx.ipn.upiicsa.web.controlacceso.external.jpa.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "cca02_rol")
public class RolJpa {
    @Id
    @Column(name = "id_rol")
    private Integer id;
    @Column(name = "tx_nombre")
    private String nombre;
    @Column(name = "tx_descripcion")
    private String descripcion;
    @Column(name = "st_activo")
    private Boolean activo;
    @OneToMany(mappedBy = "rol")
    public List<UsuarioJpa> usuarios;
}
