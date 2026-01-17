package mx.ipn.upiicsa.web.controlacceso.internal.bs.implement;

import mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.BloqueCitaRepository;
import mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.CitaRepository;
import mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.EmpleadoRepository;
import mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.ServicioRepository;
import mx.ipn.upiicsa.web.controlacceso.external.mvc.dto.AgendarCitaDto;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.BloqueCita;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Cita;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Usuario;
import mx.ipn.upiicsa.web.controlacceso.internal.input.CitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de la lógica de negocio para la gestión de citas.
 * Se encarga de calcular horarios disponibles, asignar empleados y persistir
 * las citas.
 */
@Service
@Transactional
public class CitaBs implements CitaService {

    @Autowired
    private BloqueCitaRepository bloqueRepo;
    @Autowired
    private CitaRepository citaRepo; // Instancia inyectada del repositorio
    @Autowired
    private ServicioRepository servicioRepo;
    @Autowired
    private EmpleadoRepository empleadoRepo;

    private static final Integer SUCURSAL_DEFAULT = 1;
    private static final Integer LISTA_PRECIO_DEFAULT = 1;

    /**
     * Calcula los horarios libres restando los bloques ocupados al horario laboral.
     */
    @Override
    public List<LocalTime> obtenerHorariosDisponibles(LocalDate fecha, Integer idServicio,
            Integer idEmpleadoPreferido) {
        var servicio = servicioRepo.findById(idServicio).orElseThrow();
        int duracion = servicio.getDuracion();

        List<LocalTime> horarios = new ArrayList<>();
        LocalTime inicioDia = LocalTime.of(9, 0); // Abre a las 9:00 AM
        LocalTime finDia = LocalTime.of(18, 0); // Cierra a las 6:00 PM
        LocalDateTime inicioRango = fecha.atTime(inicioDia);
        LocalDateTime finRango = fecha.atTime(finDia);

        // Traemos todos los bloques ocupados de ese día
        List<BloqueCita> bloquesOcupados = bloqueRepo.encontrarBloquesEnRango(inicioRango, finRango);

        LocalTime pivote = inicioDia;
        while (pivote.plusMinutes(duracion).isBefore(finDia) || pivote.plusMinutes(duracion).equals(finDia)) {
            boolean horaOcupada = false;
            LocalDateTime momentoCita = fecha.atTime(pivote);

            for (BloqueCita b : bloquesOcupados) {
                // Si el bloque coincide con el horario que evaluamos
                if (b.getFechaInicio().equals(momentoCita)) {
                    // Lógica de preferencia:
                    if (idEmpleadoPreferido != null) {
                        // Si el cliente quiere a Juan, verificamos si este bloque pertenece a Juan
                        var citaOriginal = citaRepo.findById(b.getIdCita()).orElse(null);
                        if (citaOriginal != null && citaOriginal.getIdEmpleado().equals(idEmpleadoPreferido)) {
                            horaOcupada = true;
                            break;
                        }
                    } else {
                        // Si es "Cualquiera", asumimos ocupado por simplicidad (v1.0)
                        horaOcupada = true;
                        break;
                    }
                }
            }

            if (!horaOcupada) {
                horarios.add(pivote);
            }
            pivote = pivote.plusMinutes(duracion);
        }
        return horarios;
    }

    /**
     * Guarda la Cita y bloquea el horario en la agenda.
     */
    @Override
    public void agendarCita(AgendarCitaDto dto, Usuario usuario) {

        Integer idEmpleadoFinal;

        // 1. DETERMINAR EL EMPLEADO
        if (dto.getIdEmpleado() != null) {
            idEmpleadoFinal = dto.getIdEmpleado();
        } else {
            // Asignación automática al primero disponible (Simplificado)
            idEmpleadoFinal = empleadoRepo.findAll()
                    .stream()
                    .findFirst()
                    .map(e -> e.getId())
                    .orElseThrow(() -> new RuntimeException("No hay empleados disponibles."));
        }

        // 2. Guardar Cita
        Cita nuevaCita = Cita.builder()
                .idPersona(usuario.getId())
                .idServicio(dto.getIdServicio())
                .idSucursal(SUCURSAL_DEFAULT)
                .idEmpleado(idEmpleadoFinal)
                .idListaPrecio(LISTA_PRECIO_DEFAULT)
                .pagado(true)
                .build();

        nuevaCita = citaRepo.save(nuevaCita);

        // 3. Guardar Bloque de Tiempo (Para que nadie más lo tome)
        var servicio = servicioRepo.findById(dto.getIdServicio()).orElseThrow();
        LocalDateTime inicio = dto.getFecha().atTime(dto.getHora());
        LocalDateTime fin = inicio.plusMinutes(servicio.getDuracion());

        BloqueCita bloque = BloqueCita.builder()
                .idSucursal(SUCURSAL_DEFAULT)
                .idCita(nuevaCita.getId())
                .fechaInicio(inicio)
                .fechaFin(fin)
                .build();

        bloqueRepo.save(bloque);
    }

    /**
     * NUEVO MÉTODO: Consulta citas donde el usuario es Cliente O Empleado.
     */
    @Override
    public List<Cita> consultarCitasPorUsuario(Integer idUsuario) {
        // Usamos la instancia inyectada 'citaRepo', NO la clase estática
        return citaRepo.findMisCitas(idUsuario);
    }
}