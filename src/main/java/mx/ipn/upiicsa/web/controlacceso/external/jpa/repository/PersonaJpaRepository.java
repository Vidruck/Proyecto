package mx.ipn.upiicsa.web.controlacceso.external.jpa.repository;

import mx.ipn.upiicsa.web.controlacceso.external.jpa.model.PersonaJpa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonaJpaRepository extends JpaRepository<PersonaJpa, Integer> {
}
