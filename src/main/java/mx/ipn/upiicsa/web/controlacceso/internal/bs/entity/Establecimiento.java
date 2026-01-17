package mx.ipn.upiicsa.web.controlacceso.internal.bs.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * Entidad que representa un establecimiento o negocio principal.
 */
@Entity
@Table(name = "tce01_establecimiento")
public class Establecimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_establecimiento")
    private Integer id;

    @Column(name = "tx_nombre")
    private String nombre;
}