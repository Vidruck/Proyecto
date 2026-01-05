package mx.ipn.upiicsa.web.controlacceso.external.mvc.dto;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class AltaEmpleadoDto {
    //Datos Personales
    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;
    private Integer idGenero;
    //Datos del usuario
    private String email;
    private String password;
    //Datos laborales
    private Integer idSucursal;
}
