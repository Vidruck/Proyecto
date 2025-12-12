package mx.ipn.upiicsa.web.controlacceso.internal.bs.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Genero {
    private Integer id;
    private String descripcion;
}
