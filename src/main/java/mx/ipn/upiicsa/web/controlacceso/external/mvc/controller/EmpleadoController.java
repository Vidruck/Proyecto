package mx.ipn.upiicsa.web.controlacceso.external.mvc.controller;

import mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.EmpleadoRepository;
// import mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.PersonaJpaRepository; // <-- YA NO LO NECESITAMOS
import mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.SucursalRepository;
import mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.UsuarioJpaRepository;
import mx.ipn.upiicsa.web.controlacceso.external.mvc.dto.AltaEmpleadoDto;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Empleado;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Persona;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Sucursal;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Usuario;
import mx.ipn.upiicsa.web.controlacceso.external.jpa.model.UsuarioJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Controller
@RequestMapping("/empleados")
public class EmpleadoController {

    @Autowired
    private SucursalRepository sucursalRepo;
    // @Autowired private PersonaJpaRepository personaRepo; // <-- ELIMINAR O COMENTAR
    @Autowired
    private UsuarioJpaRepository usuarioRepo;
    @Autowired
    private EmpleadoRepository empleadoRepo;

    @GetMapping("/alta")
    public String mostrarAlta(Model model) {
        model.addAttribute("empleadoDto", new AltaEmpleadoDto());
        model.addAttribute("sucursales", sucursalRepo.findAll());
        return "empleados/alta";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute AltaEmpleadoDto dto, RedirectAttributes redirectAttrs) {

        // 1. Crear el objeto Persona (SIN GUARDARLO AÚN)
        Persona persona = Persona.builder()
                .nombre(dto.getNombre())
                .primerApellido(dto.getPrimerApellido())
                .segundoApellido(dto.getSegundoApellido())
                .fechaNacimiento(dto.getFechaNacimiento())
                .idGenero(dto.getIdGenero())
                .build();

        // 2. Recuperar la Sucursal
        Sucursal sucursal = sucursalRepo.findById(dto.getIdSucursal()).orElseThrow();

        // 3. Crear el Empleado vinculando la Persona y la Sucursal
        Empleado empleado = Empleado.builder()
                .persona(persona) // Asignamos la persona nueva (transient)
                .sucursal(sucursal)
                .build();

        // 4. GUARDAR EMPLEADO (Esto guardará a la Persona por cascada y generará el ID)
        empleado = empleadoRepo.save(empleado);

        // Ahora empleado.getId() ya tiene el valor generado por la base de datos
        Integer idGenerado = empleado.getId();

        // 5. Crear y Guardar el Usuario (Usando el ID generado)
        String passHash = encriptar(dto.getPassword());
        Usuario usuario = Usuario.builder()
                .id(idGenerado) // Mismo ID
                .idRol(2)       // Empleado
                .login(dto.getEmail())
                .password(passHash)
                .activo(true)
                .build();

        usuarioRepo.save(UsuarioJpa.fromEntity(usuario));

        redirectAttrs.addFlashAttribute("mensajeExito", "¡Barbero registrado y asignado correctamente!");
        return "redirect:/empleados/alta";
    }

    private String encriptar(String raw) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] hash = digest.digest(raw.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}