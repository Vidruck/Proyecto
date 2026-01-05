package mx.ipn.upiicsa.web.controlacceso.internal.bs.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tci03_lista_precio")
public class ListaPrecio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lista_precio")
    private Integer id;

    @Column(name = "tx_nombre")
    private String nombre;
}