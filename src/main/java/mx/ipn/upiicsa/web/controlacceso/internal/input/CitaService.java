package mx.ipn.upiicsa.web.controlacceso.internal.input;

import mx.ipn.upiicsa.web.controlacceso.external.mvc.dto.AgendarCitaDto;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Servicio;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Usuario;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CitaService {
  List<LocalTime> obtenerHorariosDisponibles(LocalDate fecha, Integer id_servicio);
  void agendarCita(AgendarCitaDto dto, Usuario usuario);
}
