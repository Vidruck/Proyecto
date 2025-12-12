package mx.ipn.upiicsa.web.controlacceso.external.mvc.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class LoginDto {
    @NotBlank(message = "Favor de proporcionar el nombre de usuario")
    @Email(message = "El nombre de usuario es incorrecto, favor de proporcionar un correo electrónico")
    private String username;
    @NotBlank(message = "Favor de proporcionar la contraseña")
    private String password;
}
