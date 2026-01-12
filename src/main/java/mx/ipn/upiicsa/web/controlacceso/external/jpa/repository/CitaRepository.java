package mx.ipn.upiicsa.web.controlacceso.external.jpa.repository;

import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {

    // Método original (mantener por compatibilidad si se usa en otro lado)
    List<Cita> findByIdPersonaOrderByIdDesc(Integer idPersona);

    @Query("SELECT c FROM Cita c WHERE c.idPersona = :id OR c.idEmpleado = :id ORDER BY c.id DESC")
    List<Cita> findMisCitas(@Param("id") Integer id);
}