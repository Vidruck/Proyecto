package mx.ipn.upiicsa.web.controlacceso.internal.output;

import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Genero;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Persona;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Usuario;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz de salida para las operaciones de persistencia relacionadas con el
 * login.
 * Define los métodos que deben implementar los adaptadores de persistencia
 * (DAOs).
 */
public interface LoginRepository {
    Optional<Persona> findByLoginAndPassword(String login, String password);

    Integer savePersona(Persona build);

    void saveUsuario(Usuario build);

    List<Genero> findAllGeneros();
}
