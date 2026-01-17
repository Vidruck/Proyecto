package mx.ipn.upiicsa.web.controlacceso.external.jpa.repository;

import mx.ipn.upiicsa.web.controlacceso.external.jpa.model.PersonaJpa;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para gestionar la entidad {@link PersonaJpa}.
 * Permite realizar operaciones CRUD sobre los datos personales en el sistema.
 *
 */
public interface PersonaJpaRepository extends JpaRepository<PersonaJpa, Integer> {
}
