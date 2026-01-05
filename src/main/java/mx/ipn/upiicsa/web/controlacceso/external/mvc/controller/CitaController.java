package mx.ipn.upiicsa.web.controlacceso.external.mvc.controller;

import jakarta.servlet.http.HttpSession;
import mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.CitaRepository;
import mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.ServicioRepository;
import mx.ipn.upiicsa.web.controlacceso.external.mvc.dto.AgendarCitaDto;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Persona;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Servicio;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.ServicioListaPrecio;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.pk.ServicioListaPrecioPK;
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
    @Autowired
    private mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.ServicioListaPrecioRepository precioRepo;

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

        //  En lugar de enviar servicios sueltos, enviamos la relación con precio
        // Esto busca en la tabla tci02_servicio_lista_precio
        model.addAttribute("itemsServicio", precioRepo.findAll());

        AgendarCitaDto dto = new AgendarCitaDto();
        if (idServicio != null) dto.setIdServicio(idServicio);
        if (fecha != null) dto.setFecha(fecha);
        model.addAttribute("citaDto", dto);

        if (idServicio != null && fecha != null) {
            List<LocalTime> horariosLibres = citaService.obtenerHorariosDisponibles(fecha, idServicio);
            model.addAttribute("horariosLibres", horariosLibres);
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

        // 1. Buscamos el servicio (para el nombre)
        Servicio servicio = servicioRepository.findById(citaDto.getIdServicio()).orElseThrow();
        model.addAttribute("servicio", servicio);

        // 2. Buscamos el PRECIO REAL (ID Servicio + Lista 1)
        ServicioListaPrecioPK pk = new ServicioListaPrecioPK(citaDto.getIdServicio(), 1);
        ServicioListaPrecio itemPrecio = precioRepo.findById(pk).orElseThrow();

        // Pasamos el precio suelto a la vista
        model.addAttribute("precio", itemPrecio.getPrecio());

        return "citas/pago";
    }

    // 4. FINALIZAR (Guardar en BD)
    @PostMapping("/finalizar")
    public String finalizarCita(@ModelAttribute AgendarCitaDto citaDto, HttpSession session, RedirectAttributes ra) {
        Persona persona = (Persona) session.getAttribute("persona");
        if(persona == null) return "redirect:/";

        citaService.agendarCita(citaDto, persona.getUsuario());

        ra.addFlashAttribute("mensajeExito", "¡Cita pagada y agendada correctamente!");
        return "redirect:/citas/mis-citas";
    }
}