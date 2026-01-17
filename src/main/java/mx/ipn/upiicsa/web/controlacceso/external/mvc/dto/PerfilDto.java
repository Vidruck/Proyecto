package mx.ipn.upiicsa.web.controlacceso.external.mvc.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO para visualizar y editar la información de perfil del usuario actual.
 * Permite cambiar datos personales y contraseña.
 *
 */
@Data
public class PerfilDto {
    private String nombre;
    private String primerApellido;
    private String segundoApellido;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;

    private String login; // Solo lectura
    private String password; // Opcional
}
