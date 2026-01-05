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

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioJpaRepository usuarioRepo;

    @GetMapping
    public String listarUsuarios(Model model) {
        List<UsuarioJpa> usuarios = usuarioRepo.findAll();
        model.addAttribute("usuarios", usuarios);
        return "usuarios/lista";
    }

    @PostMapping("/cambiar-rol")
    public String cambiarRol(@RequestParam Integer idUsuario, @RequestParam Integer nuevoRol, RedirectAttributes redirectAttrs) {
        Optional<UsuarioJpa> usuarioOpt = usuarioRepo.findById(idUsuario);
        if (usuarioOpt.isPresent()) {
            UsuarioJpa usuario = usuarioOpt.get();
            usuario.setIdRol(nuevoRol);
            usuarioRepo.save(usuario);
            redirectAttrs.addFlashAttribute("mensajeExito", "Rol actualizado correctamente.");
        }
        return "redirect:/usuarios";
    }
}