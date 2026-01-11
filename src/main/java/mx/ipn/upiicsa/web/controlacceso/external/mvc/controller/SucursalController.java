package mx.ipn.upiicsa.web.controlacceso.external.mvc.controller;

import mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.SucursalRepository;
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

    @GetMapping
    public String listarSucursales(Model model) {
        model.addAttribute("sucursales", sucursalRepo.findAll());
        return "sucursales/lista";
    }

    @PostMapping("/eliminar")
    public String eliminarSucursal(@RequestParam Integer id, RedirectAttributes ra) {
        try {
            sucursalRepo.deleteById(id);
            ra.addFlashAttribute("mensajeExito", "Sucursal eliminada correctamente.");
        } catch (DataIntegrityViolationException e) {
            // Este error salta si la sucursal tiene empleados o citas
            ra.addFlashAttribute("mensajeError", "No se puede eliminar: Esta sucursal tiene empleados o historial activo.");
        } catch (Exception e) {
            ra.addFlashAttribute("mensajeError", "Ocurrió un error inesperado al eliminar.");
        }
        return "redirect:/sucursales";
    }
}