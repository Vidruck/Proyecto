package mx.ipn.upiicsa.web.controlacceso.external.mvc.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Signin;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SigninDto {
    @NotNull(message = "Favor de proporcionar el genero")
    private Integer idGenero;
    @NotEmpty(message = "Favor de proporcionar el nombre")
    private String nombre;
    @NotEmpty(message = "Favor de proporcionar el primer apellido")
    private String primerApellido;
    @NotEmpty(message = "Favor de proporcionar el segundo apellido")
    private String segundoApellido;
    @NotNull(message = "Favor de proporcionar la fecha de nacimiento")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaNacimiento;
    @NotEmpty(message = "Favor de proporcionar el correo electrónico con el que ingresará al sistema")
    @Email(message = "El nombre de usuario es incorrecto, favor de proporcionar un correo electrónico")
    private String login;
    @NotEmpty(message = "Favor de proporcionar la contraseña de acceso")
    private String password;

    public Signin toEntity() {
        return Signin.builder()
                .idGenero(this.idGenero)
                .nombre(this.nombre)
                .primerApellido(this.primerApellido)
                .segundoApellido(this.segundoApellido)
                .fechaNacimiento(this.fechaNacimiento)
                .login(this.login)
                .password(this.password)
                .build();
    }
}
