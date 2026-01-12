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

@Controller
@RequestMapping("/servicios")
public class ServicioController {

    @Autowired
    private ServicioRepository servicioRepo;

    @Autowired
    private ServicioListaPrecioRepository precioRepo;

    // 1. EL HUB: Lista de Servicios (Índice)
    @GetMapping
    public String listarServicios(Model model) {
        model.addAttribute("servicios", servicioRepo.findAll());
        return "servicios/lista";
    }

    // 2. Formulario de Alta
    @GetMapping("/alta")
    public String mostrarFormAlta(Model model) {
        // Enviamos un objeto servicio vacío y un precio en 0
        model.addAttribute("servicio", new Servicio());
        model.addAttribute("precio", 0);
        return "servicios/alta";
    }

    // 3. Guardar Nuevo Servicio
    // Recibimos los campos sueltos o el objeto Servicio para evitar errores con DTOs internos
    @PostMapping("/guardar")
    public String guardarServicio(@ModelAttribute Servicio servicio,
                                  @RequestParam("precio") Integer precioInput,
                                  RedirectAttributes ra) {

        // A. Guardar Servicio Base
        servicio.setActivo(1); // Nace activo
        Servicio guardado = servicioRepo.save(servicio);

        // B. Guardar Precio Inicial (Lista 1 por defecto)
        ServicioListaPrecioPK pk = new ServicioListaPrecioPK(guardado.getId(), 1);

        ServicioListaPrecio precioObj = ServicioListaPrecio.builder()
                .id(pk)
                .precio(precioInput)
                .servicio(guardado)
                .listaPrecio(ListaPrecio.builder().id(1).build()) // Referencia a Lista 1
                .build();

        precioRepo.save(precioObj);

        ra.addFlashAttribute("mensajeExito", "Paquete creado correctamente.");
        return "redirect:/servicios"; // Regresa a la LISTA
    }

    // 4. Activar/Desactivar
    @PostMapping("/cambiar-estado")
    public String cambiarEstado(@RequestParam Integer id, RedirectAttributes ra) {
        servicioRepo.findById(id).ifPresent(s -> {
            // Toggle: Si es 1 -> 0, si es 0 -> 1
            int estado = (s.getActivo() != null && s.getActivo() == 1) ? 0 : 1;
            s.setActivo(estado);
            servicioRepo.save(s);
        });
        ra.addFlashAttribute("mensajeExito", "Estado actualizado.");
        return "redirect:/servicios";
    }
}