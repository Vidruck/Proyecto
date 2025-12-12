package mx.ipn.upiicsa.web.controlacceso.external.jpa.repository;

import mx.ipn.upiicsa.web.controlacceso.external.jpa.model.GeneroJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneroJpaRepository  extends JpaRepository<GeneroJpa, Integer> {
}
