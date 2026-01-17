package mx.ipn.upiicsa.web.controlacceso.internal.bs.entity;

import lombok.*;

import java.time.LocalDate;

@Builder
@Setter
@Getter
/**
 * POJO utilizado para la transferencia de datos durante el registro o inicio de
 * sesión.
 * No es una entidad persistente.
 */
public class Signin {
    private Integer idGenero;
    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private LocalDate fechaNacimiento;
    private String login;
    private String password;
}
