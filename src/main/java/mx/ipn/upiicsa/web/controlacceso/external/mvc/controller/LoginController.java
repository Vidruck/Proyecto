package mx.ipn.upiicsa.web.controlacceso.external.mvc.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import mx.ipn.upiicsa.web.controlacceso.external.mvc.dto.SigninDto;
import mx.ipn.upiicsa.web.controlacceso.external.mvc.dto.LoginDto;
import mx.ipn.upiicsa.web.controlacceso.internal.input.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador MVC para gestionar el inicio de sesión (Login), registro de
 * nuevos usuarios (Signin)
 * y cierre de sesión (Logout).
 * Mapea a las rutas raíz "/" y "/welcome".
 *
 */
@Slf4j
@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * Muestra la página de inicio (Login) o redirige al Dashboard si ya existe
     * sesión.
     *
     * @param model   Modelo de la vista.
     * @param session Sesión actual.
     * @return Vista "index" (login) o redirección a "/welcome".
     */
    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        // Opcional: Si ya hay sesión, redirigir directo al welcome
        if (session.getAttribute("persona") != null) {
            return "redirect:/welcome";
        }
        model.addAttribute("loginDto", new LoginDto());
        return "index";
    }

    /**
     * Muestra el Dashboard principal después de un login exitoso.
     *
     * @param session Sesión actual.
     * @param model   Modelo de la vista.
     * @return Vista "welcome".
     */
    @GetMapping("/welcome")
    public String showDashboard(HttpSession session, Model model) {
        // 1. Verificar seguridad: ¿Existe el usuario en sesión?
        if (session.getAttribute("persona") == null) {
            // Si no hay sesión, lo mandamos al login
            return "redirect:/";
        }

        /*
         * 2. Si todo bien mostramos la vista welcome.html
         * Thymeleaf ya tiene acceso al objeto "session", así que no hace falta
         * agregarlo al modelo manualmente
         */
        return "welcome";
    }

    /**
     * Procesa las credenciales de inicio de sesión.
     * Si son correctas, establece la sesión y redirige al dashboard.
     * Si no, muestra error en la misma vista.
     *
     * @param loginDto      DTO con usuario y contraseña.
     * @param bindingResult Resultados de la validación.
     * @param model         Modelo para mensajes de error.
     * @param session       Sesión HTTP.
     * @return Vista "welcome" o vuelta a "index" con errores.
     */
    @PostMapping("/")
    public String login(@Valid @ModelAttribute LoginDto loginDto, BindingResult bindingResult, Model model,
            HttpSession session) {
        String resultado = null;
        var resultadoLogin = loginService.login(loginDto);
        if (resultadoLogin.isRight()) {
            var persona = resultadoLogin.get();
            log.info("LOGIN EXITOSO");
            log.info("Persona: {} {} {}", persona.getNombre(), persona.getPrimerApellido(),
                    persona.getSegundoApellido());
            log.info("Usuario: {} {} {}", persona.getUsuario().getLogin(), persona.getUsuario().getPassword(),
                    persona.getUsuario().getActivo());
            model.addAttribute("persona", persona);
            session.setAttribute("persona", persona);
            resultado = "welcome";
        } else {
            log.info("LOGIN NO ENCONTRADO: {}", resultadoLogin.getLeft());
            // ObjectError error = new ObjectError("peticion","Error, usuario y/o contraseña
            // incorrectos");
            // bindingResult.addError(error);
            if (resultadoLogin.getLeft() == 1) {
                model.addAttribute("errorLogin", "Error, usuario y/o contraseña incorrectos");
            } else {
                model.addAttribute("errorLogin", "El usuario está inactivo");
            }
            resultado = "index";
        }
        return resultado;
    }

    /**
     * Muestra el formulario de registro de nuevos usuarios (Signin).
     *
     * @param model Modelo para el DTO.
     * @return Vista "signin".
     */
    @GetMapping("/signin")
    public String signin(Model model) {
        model.addAttribute("signinDto", SigninDto.builder().build());
        return "signin";
    }

    /**
     * Procesa el registro de un nuevo usuario.
     *
     * @param signinDto          DTO con datos de registro.
     * @param bindingResult      Resultados de validación.
     * @param model              Modelo.
     * @param redirectAttributes Mensajes flash.
     * @return Redirección al login en caso de éxito, o vuelta al formulario si hay
     *         errores.
     */
    @PostMapping("/signin")
    public String signin(@Valid @ModelAttribute SigninDto signinDto, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes) {
        String resultado;
        for (ObjectError error : bindingResult.getAllErrors()) {
            log.info("ERROR: {} {}", error.getObjectName(), error.getDefaultMessage());
        }
        log.info("Valores del registro: {} {} {} {} {}", signinDto.getIdGenero(), signinDto.getNombre(),
                signinDto.getPrimerApellido(), signinDto.getSegundoApellido(), signinDto.getFechaNacimiento());
        if (!bindingResult.hasErrors()) {
            loginService.signin(signinDto.toEntity());
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "¡Cuenta creada exítosamente! Por favor inicia Sesión.");
            return "redirect:/";
        } else {
            resultado = "signin";
        }
        return resultado;
    }

    /**
     * Cierra la sesión actual (Logout).
     *
     * @param session Sesión a invalidar.
     * @return Redirección a la página de inicio.
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Destruye la sesión y borra los datos
        return "redirect:/"; // Ahora sí, al ir al inicio, pedirá login
    }
}