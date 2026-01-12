package mx.ipn.upiicsa.web.controlacceso.internal.bs.implement;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import mx.ipn.upiicsa.web.controlacceso.external.mvc.dto.LoginDto;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Genero;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Persona;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Signin;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Usuario;
import mx.ipn.upiicsa.web.controlacceso.internal.input.LoginService;
import mx.ipn.upiicsa.web.controlacceso.internal.output.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

@Slf4j
@Service
public class LoginBs implements LoginService {

    @Autowired
    private LoginRepository loginRepository;
    private List<Genero> cacheGeneros = null;

    @Override
    public Either<Integer, Persona> login(LoginDto loginDto) {
        // 1. Encriptamos la contraseña que viene del formulario ANTES de buscar en la BD
        String passwordHash = encriptarPassword(loginDto.getPassword());

        // 2. Se  busca usando el hash, no el texto plano
        var resultadoLogin = loginRepository.findByLoginAndPassword(loginDto.getUsername(), passwordHash);

        Either<Integer, Persona> resultado;
        if(resultadoLogin.isPresent()) {
            var persona = resultadoLogin.get();
            log.info("El usuario {} se autenticó exitosamente", loginDto.getUsername());
            if(!persona.getUsuario().getActivo()) {
                resultado = Either.left(2);
            } else {
                resultado = Either.right(persona);
            }
        } else {
            resultado = Either.left(1);
            log.info("Error en la autenticación del usuario");
        }
        return resultado;
    }

    @Override
    public Either<Integer, Boolean> signin(Signin signin) {
        // Guardamos la Persona primero
        Integer idPersona = loginRepository.savePersona(Persona.builder()
                .idGenero(signin.getIdGenero())
                .nombre(signin.getNombre())
                .primerApellido(signin.getPrimerApellido())
                .segundoApellido(signin.getSegundoApellido())
                .fechaNacimiento(signin.getFechaNacimiento())
                .build());
        // --- INICIO DE LA TRAMPA DE DEBUG ---


        // 3. Encriptamos la contraseña ANTES de guardarla en la BD
        String passwordHash = encriptarPassword(signin.getPassword());

        loginRepository.saveUsuario(Usuario.builder()
                .id(idPersona)
                .idRol(3)
                .login(signin.getLogin())
                .password(passwordHash)
                .activo(true)
                .build());
        return Either.right(true);
    }

    @Override
    public List<Genero> getGeneros() {
        if (this.cacheGeneros == null) {
            log.info("Generando listado de generos disponibles...");
            this.cacheGeneros = loginRepository.findAllGeneros();
        } else {
            log.info("Recuperando desde caché...");
        }
        return this.cacheGeneros;
    }

    // --- MÉTODO PRIVADO PARA ENCRIPTAR (SHA-512 + Base64) ---
    private String encriptarPassword(String passwordPlano) {
        if (passwordPlano == null) return null;
        try {
            // Algoritmo SHA-512
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            // Generar el hash en bytes
            byte[] hashBytes = digest.digest(passwordPlano.getBytes(StandardCharsets.UTF_8));
            // Convertir bytes a String Base64 para que se vea "bonito" en la BD
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            log.error("Error al encriptar contraseña", e);
            throw new RuntimeException("Error interno de seguridad", e);
        }
    }
}