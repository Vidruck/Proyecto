package mx.ipn.upiicsa.web.controlacceso.external.jpa.repository;

import mx.ipn.upiicsa.web.controlacceso.external.jpa.model.UsuarioJpa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio JPA para gestionar la entidad {@link UsuarioJpa}.
 * Provee métodos de autenticación.
 *
 */
public interface UsuarioJpaRepository extends JpaRepository<UsuarioJpa, Integer> {

    /**
     * Busca un usuario por sus credenciales (login y password).
     *
     * @param login    Nombre de usuario o correo.
     * @param password Contraseña (hash o texto plano según implementación).
     * @return Optional conteniendo el usuario si las credenciales son correctas.
     */
    Optional<UsuarioJpa> findByLoginAndPassword(String login, String password);
}
