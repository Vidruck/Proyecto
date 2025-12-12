package mx.ipn.upiicsa.web.controlacceso.external.jpa.repository;

import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.BloqueCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BloqueCitaRepository extends JpaRepository<BloqueCita, Integer> {

    // Esta consulta busca cualquier bloque que empiece dentro del rango que le demos.
    // Es lo que usa el servicio para saber qué horas pintar en rojo/ocupado.
    @Query("SELECT b FROM BloqueCita b WHERE b.fechaInicio >= :inicio AND b.fechaInicio < :fin")
    List<BloqueCita> encontrarBloquesEnRango(LocalDateTime inicio, LocalDateTime fin);
}