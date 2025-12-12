package mx.ipn.upiicsa.web.controlacceso.external.mvc.controller;

import jakarta.servlet.http.HttpSession;
import mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.ServicioRepository;
import mx.ipn.upiicsa.web.controlacceso.external.mvc.dto.AgendarCitaDto;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Persona;
import mx.ipn.upiicsa.web.controlacceso.internal.input.CitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/citas")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private ServicioRepository servicioRepository;

    @GetMapping("/agendar")
    public String mostrarAgendar(@RequestParam(required = false) Integer idServicio,
                                 @RequestParam(required = false) LocalDate fecha,
                                 Model model) {

        // 1. Cargar servicios para el Select
        model.addAttribute("servicios", servicioRepository.findAll());

        // 2. Preparar el objeto para el formulario
        AgendarCitaDto dto = new AgendarCitaDto();
        if (idServicio != null) dto.setIdServicio(idServicio);
        if (fecha != null) dto.setFecha(fecha);
        model.addAttribute("citaDto", dto);

        // 3. Si ya seleccionó fecha y servicio, buscar horarios
        if (idServicio != null && fecha != null) {
            List<LocalTime> horariosLibres = citaService.obtenerHorariosDisponibles(fecha, idServicio);
            model.addAttribute("horariosLibres", horariosLibres);
            model.addAttribute("busquedaRealizada", true);
        } else {
            model.addAttribute("busquedaRealizada", false);
        }

        return "agendar";
    }

    @PostMapping("/guardar")
    public String guardarCita(@ModelAttribute AgendarCitaDto citaDto,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {

        // Recuperar al usuario logueado desde la sesión (guardado en LoginController)
        Persona persona = (Persona) session.getAttribute("persona");

        if(persona == null) {
            return "redirect:/"; // Si no hay sesión, mandar al login
        }

        // Llamar al servicio corregido
        citaService.agendarCita(citaDto, persona.getUsuario());

        redirectAttributes.addFlashAttribute("mensajeExito", "¡Cita agendada con éxito!");
        return "redirect:/citas/agendar";
    }
}