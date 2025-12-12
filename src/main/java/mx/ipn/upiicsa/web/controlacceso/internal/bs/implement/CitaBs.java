package mx.ipn.upiicsa.web.controlacceso.internal.bs.implement; // <--- FALTABA ESTO

import mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.BloqueCitaRepository;
import mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.CitaRepository;
import mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.ServicioRepository;
import mx.ipn.upiicsa.web.controlacceso.external.mvc.dto.AgendarCitaDto; // <--- FALTABA ESTO
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

    // Valores por defecto para cumplir con la BD (ya que no los pedimos en pantalla aún)
    private static final Integer SUCURSAL_DEFAULT = 1;
    private static final Integer EMPLEADO_DEFAULT = 1;
    private static final Integer LISTA_PRECIO_DEFAULT = 1;

    @Override
    public List<LocalTime> obtenerHorariosDisponibles(LocalDate fecha, Integer idServicio) {
        var servicio = servicioRepo.findById(idServicio).orElseThrow();
        int duracion = servicio.getDuracion(); // Asegúrate que Servicio.java tenga el campo 'duracion' mapeado

        List<LocalTime> horarios = new ArrayList<>();
        LocalTime inicioDia = LocalTime.of(9, 0);
        LocalTime finDia = LocalTime.of(18, 0);

        LocalDateTime inicioRango = fecha.atTime(inicioDia);
        LocalDateTime finRango = fecha.atTime(finDia);

        // Buscamos bloques ocupados
        List<BloqueCita> bloquesOcupados = bloqueRepo.encontrarBloquesEnRango(inicioRango, finRango);

        LocalTime pivote = inicioDia;
        while (pivote.plusMinutes(duracion).isBefore(finDia) || pivote.plusMinutes(duracion).equals(finDia)) {
            boolean ocupado = false;
            LocalDateTime momentoCita = fecha.atTime(pivote);

            for (BloqueCita b : bloquesOcupados) {
                if (b.getFechaInicio().equals(momentoCita)) {
                    ocupado = true;
                    break;
                }
            }
            if (!ocupado) {
                horarios.add(pivote);
            }
            pivote = pivote.plusMinutes(duracion);
        }
        return horarios;
    }

    @Override
    public void agendarCita(AgendarCitaDto dto, Usuario usuario) {
        // 1. Guardar Cita
        Cita nuevaCita = Cita.builder()
                .idPersona(usuario.getId())
                .idServicio(dto.getIdServicio())
                .idSucursal(SUCURSAL_DEFAULT)
                .idEmpleado(EMPLEADO_DEFAULT)
                .idListaPrecio(LISTA_PRECIO_DEFAULT)
                .build();

        nuevaCita = citaRepo.save(nuevaCita);

        // 2. Calcular Fechas
        var servicio = servicioRepo.findById(dto.getIdServicio()).orElseThrow();
        LocalDateTime inicio = dto.getFecha().atTime(dto.getHora());
        LocalDateTime fin = inicio.plusMinutes(servicio.getDuracion());

        // 3. Guardar Bloque
        BloqueCita bloque = BloqueCita.builder()
                .idSucursal(SUCURSAL_DEFAULT)
                .idCita(nuevaCita.getId())
                .fechaInicio(inicio)
                .fechaFin(fin)
                .build();

        bloqueRepo.save(bloque);
    }
}