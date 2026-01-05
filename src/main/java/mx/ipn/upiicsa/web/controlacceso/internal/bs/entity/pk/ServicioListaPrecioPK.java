package mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.pk;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ServicioListaPrecioPK implements Serializable {
    @Column(name = "fk_id_servicio")
    private Integer idServicio;

    @Column(name = "fk_id_lista_precio")
    private Integer idListaPrecio;
}