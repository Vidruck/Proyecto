package mx.ipn.upiicsa.web.controlacceso.internal.input;

import mx.ipn.upiicsa.web.controlacceso.external.mvc.dto.AgendarCitaDto;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Cita; // <--- Importante agregar esto
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Usuario;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Definición del servicio para la gestión de citas.
 * Provee métodos para consultar disponibilidad, agendar y listar citas.
 */
public interface CitaService {
  List<LocalTime> obtenerHorariosDisponibles(LocalDate fecha, Integer id_servicio, Integer idEmpleadoPreferido);

  void agendarCita(AgendarCitaDto dto, Usuario usuario);

  List<Cita> consultarCitasPorUsuario(Integer idUsuario);
}