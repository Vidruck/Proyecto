package mx.ipn.upiicsa.web.controlacceso.external.mvc.controller;

import jakarta.servlet.http.HttpSession;
import mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.PersonaJpaRepository;
import mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.UsuarioJpaRepository;
import mx.ipn.upiicsa.web.controlacceso.external.mvc.dto.PerfilDto;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Persona;
import mx.ipn.upiicsa.web.controlacceso.external.jpa.model.PersonaJpa;
import mx.ipn.upiicsa.web.controlacceso.external.jpa.model.UsuarioJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Optional;

/**
 * Controlador MVC para gestionar el perfil del usuario.
 * Permite visualizar y editar la información personal y credenciales.
 * Mapea a la ruta "/perfil".
 *
 */
@Controller
@RequestMapping("/perfil")
public class PerfilController {

    @Autowired
    private PersonaJpaRepository personaRepo;
    @Autowired
    private UsuarioJpaRepository usuarioRepo;

    /**
     * Muestra la vista de detalle del perfil.
     * Carga los datos actuales del usuario en el DTO.
     *
     * @param session Sesión actual.
     * @param model   Modelo para el DTO.
     * @return Vista "perfil/detalle".
     */
    @GetMapping
    public String verPerfil(HttpSession session, Model model) {
        Persona personaSesion = (Persona) session.getAttribute("persona");
        if (personaSesion == null)
            return "redirect:/";

        // Pre-llenamos el DTO con los datos actuales
        PerfilDto dto = new PerfilDto();
        dto.setNombre(personaSesion.getNombre());
        dto.setPrimerApellido(personaSesion.getPrimerApellido());
        dto.setSegundoApellido(personaSesion.getSegundoApellido());
        dto.setFechaNacimiento(personaSesion.getFechaNacimiento());
        dto.setLogin(personaSesion.getUsuario().getLogin());

        model.addAttribute("perfilDto", dto);
        return "perfil/detalle";
    }

    /**
     * Actualiza la información del perfil del usuario.
     * Modifica los datos personales y, opcionalmente, la contraseña.
     * Actualiza también la sesión actual para reflejar los cambios.
     *
     * @param dto           DTO con los nuevos datos.
     * @param session       Sesión actual.
     * @param redirectAttrs Mensajes flash.
     * @return Redirección a la vista de perfil.
     */
    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute PerfilDto dto,
            HttpSession session,
            RedirectAttributes redirectAttrs) {

        Persona personaSesion = (Persona) session.getAttribute("persona");
        if (personaSesion == null)
            return "redirect:/";

        // 1. Actualizar Datos Personales en BD
        Optional<PersonaJpa> personaOpt = personaRepo.findById(personaSesion.getId());
        if (personaOpt.isPresent()) {
            PersonaJpa personaJpa = personaOpt.get();
            personaJpa.setNombre(dto.getNombre());
            personaJpa.setPrimerApellido(dto.getPrimerApellido());
            personaJpa.setSegundoApellido(dto.getSegundoApellido());
            personaJpa.setFechaNacimiento(dto.getFechaNacimiento());

            PersonaJpa guardada = personaRepo.save(personaJpa);

            // ACTUALIZAR SESIÓN (Importante para que se vea el cambio arriba)
            personaSesion.setNombre(guardada.getNombre());
            personaSesion.setPrimerApellido(guardada.getPrimerApellido());
            personaSesion.setSegundoApellido(guardada.getSegundoApellido());
            personaSesion.setFechaNacimiento(guardada.getFechaNacimiento());
        }

        // 2. Actualizar Contraseña (si el usuario escribió algo)
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            Optional<UsuarioJpa> usuarioOpt = usuarioRepo.findById(personaSesion.getId());
            if (usuarioOpt.isPresent()) {
                UsuarioJpa usuarioJpa = usuarioOpt.get();
                usuarioJpa.setPassword(dto.getPassword());
                usuarioRepo.save(usuarioJpa);
            }
        }

        session.setAttribute("persona", personaSesion); // Refrescamos sesión
        redirectAttrs.addFlashAttribute("mensajeExito", "¡Perfil actualizado correctamente!");
        return "redirect:/perfil";
    }
}