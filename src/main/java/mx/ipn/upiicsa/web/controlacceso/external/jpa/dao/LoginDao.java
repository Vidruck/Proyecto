package mx.ipn.upiicsa.web.controlacceso.external.jpa.dao;

import mx.ipn.upiicsa.web.controlacceso.external.jpa.model.PersonaJpa;
import mx.ipn.upiicsa.web.controlacceso.external.jpa.model.UsuarioJpa;
import mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.GeneroJpaRepository;
import mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.PersonaJpaRepository;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Genero;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Persona;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Usuario;
import java.util.stream.Collectors;
import mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.UsuarioJpaRepository;
import mx.ipn.upiicsa.web.controlacceso.internal.output.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class LoginDao implements LoginRepository {
    @Autowired
    private UsuarioJpaRepository usuarioJpaRepository;
    @Autowired
    private PersonaJpaRepository personaJpaRepository;
    @Autowired
    private GeneroJpaRepository generoJpaRepository;
    
    public Optional<Persona> findByLoginAndPassword(String login, String password) {
        var resultado = usuarioJpaRepository.findByLoginAndPassword(login, password);
        if(resultado.isPresent()) {
            var usuarioJpa = resultado.get();
            var persona = usuarioJpa.getPersona().toEntity();
            persona.setUsuario(usuarioJpa.toEntity());
            return Optional.of(persona);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Integer savePersona(Persona persona) {
        return personaJpaRepository.save(PersonaJpa.fromEntity(persona)).getId();
    }

    @Override
    public void saveUsuario(Usuario build) {
        usuarioJpaRepository.save(UsuarioJpa.fromEntity(build));
    }
 @Override
    public List<Genero> findAllGeneros() {
        return generoJpaRepository.findAll().stream()
                .map(jpa -> Genero.builder()
                        .id(jpa.getId())
                        .descripcion(jpa.getDescripcion())
                        .build())
                .collect(Collectors.toList());
    }
}
