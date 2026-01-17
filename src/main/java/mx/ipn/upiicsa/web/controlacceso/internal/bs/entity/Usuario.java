package mx.ipn.upiicsa.web.controlacceso.internal.bs.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
/**
 * Entidad que gestiona las credenciales de acceso al sistema.
 * Vincula un login y password con un rol y estado activo.
 */
@Entity
@Table(name = "tca02_usuario")
public class Usuario {

    @Id
    @Column(name = "id_usuario")
    private Integer id;

    @Column(name = "fk_id_rol")
    private Integer idRol;

    @Column(name = "st_activo")
    private Boolean activo;

    @Column(name = "tx_login", nullable = false, unique = true)
    private String login;

    @Column(name = "tx_password", nullable = false)
    private String password;
}