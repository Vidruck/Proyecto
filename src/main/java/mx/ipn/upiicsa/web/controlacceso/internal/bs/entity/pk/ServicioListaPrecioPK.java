package mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.pk;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * Clave primaria compuesta para la entidad
 * {@link mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.ServicioListaPrecio}.
 * Combina el ID del servicio y el ID de la lista de precios.
 */
@Embeddable
public class ServicioListaPrecioPK implements Serializable {
    @Column(name = "fk_id_servicio")
    private Integer idServicio;

    @Column(name = "fk_id_lista_precio")
    private Integer idListaPrecio;
}