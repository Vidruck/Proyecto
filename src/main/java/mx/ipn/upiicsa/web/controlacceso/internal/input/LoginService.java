package mx.ipn.upiicsa.web.controlacceso.internal.input;

import io.vavr.control.Either;
import mx.ipn.upiicsa.web.controlacceso.external.mvc.dto.LoginDto;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Genero;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Persona;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Signin;

import java.util.List;
import java.util.Optional;

/**
 * Definición del servicio de autenticación y registro de usuarios.
 * Maneja el login, registro y catálogos necesarios para el acceso.
 */
public interface LoginService {
    Either<Integer, Persona> login(LoginDto login);

    Either<Integer, Boolean> signin(Signin entity);

    List<Genero> getGeneros();
}
