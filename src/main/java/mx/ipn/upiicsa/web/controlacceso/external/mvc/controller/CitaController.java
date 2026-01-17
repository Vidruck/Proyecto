package mx.ipn.upiicsa.web.controlacceso.external.mvc.controller;

import jakarta.servlet.http.HttpSession;
import mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.CitaRepository;
import mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.EmpleadoRepository;
import mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.ServicioRepository;
import mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.ServicioListaPrecioRepository;
import mx.ipn.upiicsa.web.controlacceso.external.mvc.dto.AgendarCitaDto;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Cita;
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

/**
 * Controlador MVC para la gestión de citas por parte de los clientes.
 * Permite listar citas, agendar nuevas citas (incluyendo selección de horarios)
 * y procesar el pago simulado.
 * Mapea a la ruta "/citas".
 *
 */
@Controller
@RequestMapping("/citas")
public class CitaController {

    @Autowired
    private CitaService citaService;
    @Autowired
    private ServicioRepository servicioRepository;
    @Autowired
    private CitaRepository citaRepository;
    @Autowired
    private ServicioListaPrecioRepository precioRepo;

    @Autowired
    private EmpleadoRepository empleadoRepo;

    /**
     * Muestra el historial de citas del usuario logueado.
     *
     * @param session Sesión HTTP para recuperar al usuario actual.
     * @param model   Modelo para pasar la lista de citas a la vista.
     * @return Vista "citas/index" o redirección al login si no hay sesión.
     */
    @GetMapping("/mis-citas")
    public String verMisCitas(HttpSession session, Model model) {
        Persona persona = (Persona) session.getAttribute("persona");
        if (persona == null)
            return "redirect:/";
        model.addAttribute("citas", citaService.consultarCitasPorUsuario(persona.getId()));
        return "citas/index";
    }

    /**
     * Muestra el formulario para agendar una nueva cita.
     * Si se reciben parámetros de fecha y servicio, busca y muestra los horarios
     * disponibles.
     *
     * @param idServicio ID del servicio seleccionado (opcional para primera carga).
     * @param fecha      Fecha deseada (opcional).
     * @param idEmpleado ID del empleado preferido (opcional).
     * @param model      Modelo con listas de servicios, empleados y horarios
     *                   disponibles.
     * @return Vista "agendar".
     */
    @GetMapping("/agendar")
    public String mostrarAgendar(@RequestParam(required = false) Integer idServicio,
            @RequestParam(required = false) LocalDate fecha,
            @RequestParam(required = false) Integer idEmpleado,
            Model model) {

        // Cargar listas para los selects
        model.addAttribute("itemsServicio", precioRepo.findAll());
        model.addAttribute("empleados", empleadoRepo.findAll());

        AgendarCitaDto dto = new AgendarCitaDto();
        if (idServicio != null)
            dto.setIdServicio(idServicio);
        if (fecha != null)
            dto.setFecha(fecha);
        if (idEmpleado != null)
            dto.setIdEmpleado(idEmpleado);

        model.addAttribute("citaDto", dto);

        if (idServicio != null && fecha != null) {
            List<LocalTime> horariosLibres = citaService.obtenerHorariosDisponibles(fecha, idServicio, idEmpleado);

            model.addAttribute("horariosLibres", horariosLibres);
            model.addAttribute("busquedaRealizada", true);
        } else {
            model.addAttribute("busquedaRealizada", false);
        }
        return "agendar";
    }

    /**
     * Procesa la confirmación de la cita antes de finalizar.
     * Muestra el resumen de pago.
     *
     * @param citaDto DTO con los datos de la cita seleccionada.
     * @param model   Modelo con datos del servicio y precio.
     * @param session Sesión actual.
     * @return Vista "citas/pago" o redirección a login.
     */
    @PostMapping("/confirmar")
    public String confirmarPago(@ModelAttribute AgendarCitaDto citaDto, Model model, HttpSession session) {
        if (session.getAttribute("persona") == null)
            return "redirect:/";

        model.addAttribute("citaDto", citaDto);
        Servicio servicio = servicioRepository.findById(citaDto.getIdServicio()).orElseThrow();
        model.addAttribute("servicio", servicio);

        ServicioListaPrecioPK pk = new ServicioListaPrecioPK(citaDto.getIdServicio(), 1);
        ServicioListaPrecio itemPrecio = precioRepo.findById(pk).orElseThrow();
        model.addAttribute("precio", itemPrecio.getPrecio());

        return "citas/pago";
    }

    /**
     * Finaliza el proceso de agendado, guardando la cita en base de datos.
     * Asocia la cita al usuario en sesión.
     *
     * @param citaDto Datos finales de la cita.
     * @param session Sesión del usuario.
     * @param ra      Atributos flash para mensajes de éxito.
     * @return Redirección a "citas/mis-citas".
     */
    @PostMapping("/finalizar")
    public String finalizarCita(@ModelAttribute AgendarCitaDto citaDto, HttpSession session, RedirectAttributes ra) {
        Persona persona = (Persona) session.getAttribute("persona");
        if (persona == null)
            return "redirect:/";

        citaService.agendarCita(citaDto, persona.getUsuario());

        ra.addFlashAttribute("mensajeExito", "¡Cita pagada y agendada correctamente!");
        return "redirect:/citas/mis-citas";
    }
}