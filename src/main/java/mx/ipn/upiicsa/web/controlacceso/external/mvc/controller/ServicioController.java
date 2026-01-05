package mx.ipn.upiicsa.web.controlacceso.external.mvc.controller;

import mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.ServicioRepository;
import mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.ServicioListaPrecioRepository;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Servicio;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.ServicioListaPrecio;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.pk.ServicioListaPrecioPK;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.ListaPrecio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import lombok.Data;

@Controller
@RequestMapping("/servicios")
public class ServicioController {

    @Autowired
    private ServicioRepository servicioRepo;
    @Autowired
    private ServicioListaPrecioRepository precioRepo;

    // DTO interno simple para el formulario
    @Data
    public static class ServicioForm {
        private String nombre;
        private String descripcion;
        private Integer duracion; // En minutos
        private Integer precio;
    }

    @GetMapping("/alta")
    public String mostrarAlta(Model model) {
        model.addAttribute("servicioForm", new ServicioForm());
        return "servicios/alta";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute ServicioForm form, RedirectAttributes redirectAttrs) {

        // 1. Guardar el Servicio (Definición del Paquete)
        Servicio servicio = Servicio.builder()
                .nombre(form.getNombre())
                .descripcion(form.getDescripcion())
                .duracion(form.getDuracion())
                .activo(1) // 1 = Activo
                .build();

        servicio = servicioRepo.save(servicio);

        // 2. Asignar Precio a la Lista General (ID 1 por defecto)
        ServicioListaPrecioPK pk = new ServicioListaPrecioPK(servicio.getId(), 1);

        ServicioListaPrecio precioRelacion = ServicioListaPrecio.builder()
                .id(pk)
                .precio(form.getPrecio())
                .servicio(servicio)
                .listaPrecio(ListaPrecio.builder().id(1).build()) // Referencia dummy a la lista 1
                .build();

        precioRepo.save(precioRelacion);

        redirectAttrs.addFlashAttribute("mensajeExito", "¡Servicio/Paquete registrado correctamente!");
        return "redirect:/servicios/alta";
    }
}