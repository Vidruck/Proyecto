package mx.ipn.upiicsa.web.controlacceso.external.mvc.controller;

import jakarta.servlet.http.HttpSession;
import mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.CitaRepository;
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

    @Autowired private CitaService citaService;
    @Autowired private ServicioRepository servicioRepository;
    @Autowired private CitaRepository citaRepository;

    // 1. VER HISTORIAL
    @GetMapping("/mis-citas")
    public String verMisCitas(HttpSession session, Model model) {
        Persona persona = (Persona) session.getAttribute("persona");
        if (persona == null) return "redirect:/";
        model.addAttribute("citas", citaRepository.findByIdPersonaOrderByIdDesc(persona.getId()));
        return "citas/index";
    }

    // 2. AGENDAR (Selección)
    @GetMapping("/agendar")
    public String mostrarAgendar(@RequestParam(required = false) Integer idServicio,
                                 @RequestParam(required = false) LocalDate fecha,
                                 Model model) {
        model.addAttribute("servicios", servicioRepository.findAll());
        AgendarCitaDto dto = new AgendarCitaDto();
        if (idServicio != null) dto.setIdServicio(idServicio);
        if (fecha != null) dto.setFecha(fecha);
        model.addAttribute("citaDto", dto);

        if (idServicio != null && fecha != null) {
            model.addAttribute("horariosLibres", citaService.obtenerHorariosDisponibles(fecha, idServicio));
            model.addAttribute("busquedaRealizada", true);
        } else {
            model.addAttribute("busquedaRealizada", false);
        }
        return "agendar";
    }

    // 3. CONFIRMAR (Pantalla de Pago)
    @PostMapping("/confirmar")
    public String confirmarPago(@ModelAttribute AgendarCitaDto citaDto, Model model, HttpSession session) {
        if(session.getAttribute("persona") == null) return "redirect:/";
        model.addAttribute("citaDto", citaDto);
        model.addAttribute("servicio", servicioRepository.findById(citaDto.getIdServicio()).orElseThrow());
        return "citas/pago";
    }

    // 4. FINALIZAR (Guardar en BD)
    @PostMapping("/finalizar")
    public String finalizarCita(@ModelAttribute AgendarCitaDto citaDto, HttpSession session, RedirectAttributes ra) {
        Persona persona = (Persona) session.getAttribute("persona");
        if(persona == null) return "redirect:/";

        citaService.agendarCita(citaDto, persona.getUsuario());
        // Aquí se  podra actualizar el campo 'pagado' a true si modificas el servicio,
        // o hacerlo manualmente si recuperas la cita creada.

        ra.addFlashAttribute("mensajeExito", "¡Cita pagada y agendada correctamente!");
        return "redirect:/citas/mis-citas";
    }
}