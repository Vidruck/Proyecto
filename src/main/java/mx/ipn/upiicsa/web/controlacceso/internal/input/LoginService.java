package mx.ipn.upiicsa.web.controlacceso.internal.input;

import io.vavr.control.Either;
import mx.ipn.upiicsa.web.controlacceso.external.mvc.dto.LoginDto;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Genero;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Persona;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Signin;

import java.util.List;
import java.util.Optional;

public interface LoginService {
    Either<Integer, Persona> login(LoginDto login);
    Either<Integer, Boolean> signin(Signin entity);
    List<Genero> getGeneros();
}
