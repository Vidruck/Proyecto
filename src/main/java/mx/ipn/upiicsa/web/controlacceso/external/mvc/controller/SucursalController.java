package mx.ipn.upiicsa.web.controlacceso.external.mvc.controller;

import mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.EstablecimientoRepository;
import mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.SucursalRepository;
import mx.ipn.upiicsa.web.controlacceso.external.mvc.dto.AltaSucursalDto;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Establecimiento;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Sucursal;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/sucursales")
public class SucursalController {

    @Autowired
    private SucursalRepository sucursalRepo;

    @Autowired
    private EstablecimientoRepository establecimientoRepo; // Para asignar el negocio padre

    // 1. EL HUB: Lista de Sucursales
    @GetMapping
    public String listarSucursales(Model model) {
        model.addAttribute("sucursales", sucursalRepo.findAll());
        return "sucursales/lista";
    }

    // 2. Formulario de Alta
    @GetMapping("/alta")
    public String mostrarFormAlta(Model model) {
        model.addAttribute("sucursalDto", new AltaSucursalDto());
        return "sucursales/alta";
    }

    // 3. Guardar Nueva Sucursal
    @PostMapping("/guardar")
    public String guardarSucursal(@ModelAttribute AltaSucursalDto dto, RedirectAttributes ra) {
        try {
            Sucursal sucursal = new Sucursal();
            sucursal.setNombre(dto.getNombre());


            Establecimiento est = establecimientoRepo.findById(1).orElseThrow();
            sucursal.setEstablecimiento(est);

            // Convertir Lat/Lon a Geometría (Point)
            // PostGIS usa formato: POINT(Longitud Latitud)
            String wktInfo = "POINT(" + dto.getLongitud() + " " + dto.getLatitud() + ")";
            Point ubicacion = (Point) new WKTReader().read(wktInfo);
            ubicacion.setSRID(4326); // Sistema de coordenadas GPS estándar
            sucursal.setUbicacion(ubicacion);

            sucursalRepo.save(sucursal);
            ra.addFlashAttribute("mensajeExito", "Sucursal registrada exitosamente.");

        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("mensajeError", "Error al guardar la ubicación.");
        }
        return "redirect:/sucursales";
    }

    // 4. Eliminar
    @PostMapping("/eliminar")
    public String eliminarSucursal(@RequestParam Integer id, RedirectAttributes ra) {
        try {
            sucursalRepo.deleteById(id);
            ra.addFlashAttribute("mensajeExito", "Sucursal eliminada correctamente.");
        } catch (DataIntegrityViolationException e) {
            ra.addFlashAttribute("mensajeError", "No se puede eliminar: Tiene empleados o citas asociadas.");
        }
        return "redirect:/sucursales";
    }
}