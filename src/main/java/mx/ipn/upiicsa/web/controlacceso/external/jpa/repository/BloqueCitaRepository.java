package mx.ipn.upiicsa.web.controlacceso.external.jpa.repository;

import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.BloqueCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio JPA para gestionar la entidad {@link BloqueCita}.
 * Provee métodos para consultar la disponibilidad de horarios.
 *
 */
@Repository
public interface BloqueCitaRepository extends JpaRepository<BloqueCita, Integer> {

    /**
     * Busca bloques de cita que inicien dentro de un rango de tiempo específico.
     * Utilizado para determinar horarios ocupados.
     *
     * @param inicio Fecha y hora de inicio del rango.
     * @param fin    Fecha y hora de fin del rango.
     * @return Lista de bloques de cita que inician en el rango [inicio, fin).
     */
    @Query("SELECT b FROM BloqueCita b WHERE b.fechaInicio >= :inicio AND b.fechaInicio < :fin")
    List<BloqueCita> encontrarBloquesEnRango(LocalDateTime inicio, LocalDateTime fin);
}