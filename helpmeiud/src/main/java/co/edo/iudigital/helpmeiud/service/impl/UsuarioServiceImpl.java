package co.edo.iudigital.helpmeiud.service.impl;

import co.edo.iudigital.helpmeiud.dto.request.UsuarioDTORequest;
import co.edo.iudigital.helpmeiud.dto.response.UsuarioDTO;
import co.edo.iudigital.helpmeiud.exceptions.BadRequestException;
import co.edo.iudigital.helpmeiud.exceptions.ErrorDto;
import co.edo.iudigital.helpmeiud.exceptions.InternalServerErrorException;
import co.edo.iudigital.helpmeiud.exceptions.RestException;
import co.edo.iudigital.helpmeiud.model.Role;
import co.edo.iudigital.helpmeiud.model.Usuario;
import co.edo.iudigital.helpmeiud.repository.IUsuarioRepository;
import co.edo.iudigital.helpmeiud.service.iface.IUsuarioService;
import co.edo.iudigital.helpmeiud.util.ConstUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UsuarioServiceImpl implements IUsuarioService, UserDetailsService {

    @Value("${emailserver.enabled}")
    private Boolean emailEnabled;

    @Lazy
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired // inyección de dependencias por atriubuto
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public List<UsuarioDTO> consultarTodos() {
        return null;
    }

    @Override
    public UsuarioDTO consultarPorId(Long id) {
        return null;
    }

    @Override
    public UsuarioDTO consultarPorUsername(String username) {
        return null;
    }

    @Override
    public UsuarioDTO guardar(UsuarioDTORequest usuarioDTORequest) throws RestException {
        Usuario usuario;
        Role role = new Role();
        role.setId(2L);
        usuario = usuarioRepository.findByUsername(usuarioDTORequest.getUsername());
        if(usuario != null){
            throw new BadRequestException(
                    ErrorDto.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Este usuario ya existe")
                            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                            .date(LocalDateTime.now())
                            .build()
            );
        }
        //String encoded = passwordEncoder.encode(usuarioDTORequest.getPassword());
        // log.info("password {}", encoded);
        usuario = new Usuario();
        // convertir usuarioDTORequest a usuario
        //usuario.setPassword(encoded);
        log.info("password cifrada{}", passwordEncoder.encode(usuarioDTORequest.getPassword()));
        usuario.setNombre(usuarioDTORequest.getNombre());
        usuario.setUsername(usuarioDTORequest.getUsername());
        usuario.setPassword(passwordEncoder.encode(usuarioDTORequest.getPassword()));
        usuario.setApellido(usuarioDTORequest.getApellido());
        usuario.setImage(usuarioDTORequest.getImage());
        usuario.setEnabled(true);
        usuario.setRedSocial(false);
        usuario.setFechaNacimiento(usuarioDTORequest.getFechaNacimiento());
        usuario.setRoles(Collections.singletonList(role));

        usuario = usuarioRepository.save(usuario);
        // convertir de usuario a usuarioDTO

        if(usuario!= null && usuario.getUsername() != null) {
            if(Boolean.TRUE.equals(emailEnabled)) {
                String mensaje = "Su usuario: "+usuario.getUsername()+"; password: "+usuarioDTORequest.getPassword();
                String asunto = "Registro en HelmeIUD";
                emailService.sendEmail(
                        mensaje,
                        usuario.getUsername(),
                        asunto
                );
            }

        }
        return   UsuarioDTO.builder()
                .username(usuario.getUsername())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .enabled(usuario.getEnabled())
                .fechaNacimiento(usuario.getFechaNacimiento())
                .redSocial(usuario.getRedSocial())
                .image(usuario.getImage())
                .roles(usuario.getRoles()
                        .stream().map(r -> r.getNombre())
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    @Override
    public UsuarioDTO userInfo(Authentication authentication) throws RestException{
        if(!authentication.isAuthenticated()) {
            throw new RestException(
                    ErrorDto.builder()
                            .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                            .message(ConstUtil.MESSAGE_NOT_AUTHORIZED)
                            .status(HttpStatus.UNAUTHORIZED.value())
                            .date(LocalDateTime.now())
                            .build()
            );
        }
        String userName = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsername(userName);
        if(usuario==null) {
            throw new RestException(
                    ErrorDto.builder()
                            .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                            .message(ConstUtil.MESSAGE_NOT_AUTHORIZED)
                            .status(HttpStatus.UNAUTHORIZED.value())
                            .date(LocalDateTime.now())
                            .build()
            );
        }
        UsuarioDTO usuarioDTO = UsuarioDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .fechaNacimiento(usuario.getFechaNacimiento())
                .image(usuario.getImage())
                .roles(usuario.getRoles()
                        .stream().map(r -> r.getNombre())
                        .collect(Collectors.toList()))
                .username(usuario.getUsername())
                .build();
        return usuarioDTO;
    }

    @Override
    public Usuario actualizar(Usuario usuario) throws RestException{
        if(usuario == null) {
            throw new InternalServerErrorException(
                    ErrorDto.builder()
                            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                            .message(ConstUtil.MESSAGE_ERROR_DATA)
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .date(LocalDateTime.now())
                            .build());
        }
        if(usuario.getId() == null) {
            throw new InternalServerErrorException(
                    ErrorDto.builder()
                            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                            .message(ConstUtil.MESSAGE_ERROR_DATA)
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .date(LocalDateTime.now())
                            .build());
        }
        Boolean exists = usuarioRepository.existsById(usuario.getId());
        if(!exists) {
            throw new InternalServerErrorException(
                    ErrorDto.builder()
                            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                            .message(ConstUtil.MESSAGE_ERROR_DATA)
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .date(LocalDateTime.now())
                            .build());
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)// por ser consulta, readOnly
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username);
        if(usuario == null) {
            log.error("Error de login, no existe usuario: "+ usuario);
            throw new UsernameNotFoundException("Error de login, no existe usuario: "+ username);
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        for(Role role: usuario.getRoles()) {
            GrantedAuthority authority = new SimpleGrantedAuthority(role.getNombre());
            log.info("Rol {}", authority.getAuthority());
            authorities.add(authority);
        }
        return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true,authorities);
    }

}
