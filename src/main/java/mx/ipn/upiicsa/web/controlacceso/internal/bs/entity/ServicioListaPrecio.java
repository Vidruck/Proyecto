package mx.ipn.upiicsa.web.controlacceso.internal.bs.entity;

import jakarta.persistence.*;
import lombok.*;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.pk.ServicioListaPrecioPK;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * Entidad asociativa que define el precio de un servicio específico
 * dentro de una lista de precios determinada.
 */
@Entity
@Table(name = "tci02_servicio_lista_precio")
public class ServicioListaPrecio {

    @EmbeddedId
    private ServicioListaPrecioPK id;

    @Column(name = "nu_precio")
    private Integer precio;

    // Relaciones para navegar (opcional pero útil)
    @ManyToOne
    @MapsId("idServicio")
    @JoinColumn(name = "fk_id_servicio")
    private Servicio servicio;

    @ManyToOne
    @MapsId("idListaPrecio")
    @JoinColumn(name = "fk_id_lista_precio")
    private ListaPrecio listaPrecio;
}