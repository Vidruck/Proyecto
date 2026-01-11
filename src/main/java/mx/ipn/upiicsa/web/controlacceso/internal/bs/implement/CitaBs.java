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

@Service
@Transactional
public class CitaBs implements CitaService {

    @Autowired
    private BloqueCitaRepository bloqueRepo;
    @Autowired
    private CitaRepository citaRepo;
    @Autowired
    private ServicioRepository servicioRepo;
    @Autowired
    private EmpleadoRepository empleadoRepo;

    private static final Integer SUCURSAL_DEFAULT = 1;
    private static final Integer LISTA_PRECIO_DEFAULT = 1;

    @Override
    public List<LocalTime> obtenerHorariosDisponibles(LocalDate fecha, Integer idServicio, Integer idEmpleadoPreferido) {
        var servicio = servicioRepo.findById(idServicio).orElseThrow();
        int duracion = servicio.getDuracion();

        List<LocalTime> horarios = new ArrayList<>();
        LocalTime inicioDia = LocalTime.of(9, 0);
        LocalTime finDia = LocalTime.of(18, 0);
        LocalDateTime inicioRango = fecha.atTime(inicioDia);
        LocalDateTime finRango = fecha.atTime(finDia);

        List<BloqueCita> bloquesOcupados = bloqueRepo.encontrarBloquesEnRango(inicioRango, finRango);

        LocalTime pivote = inicioDia;
        while (pivote.plusMinutes(duracion).isBefore(finDia) || pivote.plusMinutes(duracion).equals(finDia)) {
            boolean horaOcupada = false;
            LocalDateTime momentoCita = fecha.atTime(pivote);

            for (BloqueCita b : bloquesOcupados) {
                if (b.getFechaInicio().equals(momentoCita)) {
                    // Lógica de preferencia de barbero
                    if (idEmpleadoPreferido != null) {
                        // Si el cliente quiere a uno específico, revisamos si ESE bloque es de él
                        var citaOriginal = citaRepo.findById(b.getIdCita()).orElse(null);
                        if (citaOriginal != null && citaOriginal.getIdEmpleado().equals(idEmpleadoPreferido)) {
                            horaOcupada = true;
                            break;
                        }
                    } else {
                        // Si no elige, cualquier bloque en esa hora cuenta como ocupado (modelo simple)
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

    @Override
    public void agendarCita(AgendarCitaDto dto, Usuario usuario) {

        Integer idEmpleadoFinal;

        // 1. DETERMINAR EL EMPLEADO
        if (dto.getIdEmpleado() != null) {
            // Caso A: El cliente eligió a alguien
            idEmpleadoFinal = dto.getIdEmpleado();
        } else {
            // Caso B: Le da igual ("Cualquiera"), asignamos al primero que encontremos
            idEmpleadoFinal = empleadoRepo.findAll()
                    .stream()
                    .findFirst()
                    .map(empleado -> empleado.getId())
                    .orElseThrow(() -> new RuntimeException("No hay empleados disponibles para asignar."));
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

        // 3. Guardar Bloque de Tiempo
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
}