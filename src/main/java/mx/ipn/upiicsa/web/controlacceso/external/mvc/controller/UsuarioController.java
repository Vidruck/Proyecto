package mx.ipn.upiicsa.web.controlacceso.external.mvc.controller;

import mx.ipn.upiicsa.web.controlacceso.external.jpa.model.UsuarioJpa;
import mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.UsuarioJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.Optional;

/**
 * Controlador MVC para la gestión administrativa de usuarios.
 * Permite listar usuarios, cambiar roles y activar/desactivar cuentas.
 * Mapea a la ruta "/usuarios".
 *
 */
@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioJpaRepository usuarioRepo;

    /**
     * Lista todos los usuarios registrados en el sistema.
     *
     * @param model Modelo de la vista.
     * @return Vista "usuarios/lista".
     */
    @GetMapping
    public String listarUsuarios(Model model) {
        List<UsuarioJpa> usuarios = usuarioRepo.findAll();
        model.addAttribute("usuarios", usuarios);
        return "usuarios/lista";
    }

    /**
     * Cambia el rol de un usuario específico.
     *
     * @param idUsuario     ID del usuario.
     * @param nuevoRol      ID del nuevo rol a asignar.
     * @param redirectAttrs Mensajes flash.
     * @return Redirección a la lista de usuarios.
     */
    @PostMapping("/cambiar-rol")
    public String cambiarRol(@RequestParam Integer idUsuario, @RequestParam Integer nuevoRol,
            RedirectAttributes redirectAttrs) {
        Optional<UsuarioJpa> usuarioOpt = usuarioRepo.findById(idUsuario);
        if (usuarioOpt.isPresent()) {
            UsuarioJpa usuario = usuarioOpt.get();
            usuario.setIdRol(nuevoRol);
            usuarioRepo.save(usuario);
            redirectAttrs.addFlashAttribute("mensajeExito", "Rol actualizado correctamente.");
        }
        return "redirect:/usuarios";
    }

    /**
     * Cambia el estado de activo a inactivo y viceversa (Toggle).
     * Utilizado para bloquear accesos o marcar personal de vacaciones.
     *
     * @param idUsuario     ID del usuario.
     * @param RedirectAttrs Mensajes flash.
     * @return Redirección a la lista de usuarios.
     */
    @PostMapping("/cambiar-estado")
    public String cambiarEstado(@RequestParam Integer idUsuario, RedirectAttributes RedirectAttrs) {
        Optional<UsuarioJpa> usuarioOpt = usuarioRepo.findById(idUsuario);
        if (usuarioOpt.isPresent()) {
            UsuarioJpa usuario = usuarioOpt.get();
            // invierte el estado (si es true --> flase, si es false --> true)
            boolean nuevoEstado = !usuario.getActivo();
            usuario.setActivo(nuevoEstado);
            usuarioRepo.save(usuario);

            String estadoTexto = nuevoEstado ? "Activado (Disponible)" : "Desactivado (Vacaiones/baja)";
            RedirectAttrs.addFlashAttribute("mensajeExito", "El usuario ha sido " + estadoTexto);
        }
        return "redirect:/usuarios";
    }
}