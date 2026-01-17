package mx.ipn.upiicsa.web.controlacceso.external.jpa.repository;

import mx.ipn.upiicsa.web.controlacceso.external.jpa.model.GeneroJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para gestionar la entidad {@link GeneroJpa}.
 * Mantiene el catálogo de géneros.
 *
 */
@Repository
public interface GeneroJpaRepository extends JpaRepository<GeneroJpa, Integer> {
}
