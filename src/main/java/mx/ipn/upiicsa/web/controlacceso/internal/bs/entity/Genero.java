package mx.ipn.upiicsa.web.controlacceso.internal.bs.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
/**
 * POJO que representa la información de género.
 * No es una entidad JPA persistente en este contexto, sino un objeto de
 * transferencia o mapeo simple.
 */
public class Genero {
    private Integer id;
    private String descripcion;
}
