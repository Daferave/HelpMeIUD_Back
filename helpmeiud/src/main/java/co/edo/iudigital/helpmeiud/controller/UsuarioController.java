package co.edo.iudigital.helpmeiud.controller;


import co.edo.iudigital.helpmeiud.dto.request.UsuarioDTORequest;
import co.edo.iudigital.helpmeiud.dto.response.UsuarioDTO;
import co.edo.iudigital.helpmeiud.exceptions.*;
import co.edo.iudigital.helpmeiud.model.Usuario;
import co.edo.iudigital.helpmeiud.service.iface.IUsuarioService;
import co.edo.iudigital.helpmeiud.util.ConstUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private IUsuarioService usuarioService;

    @PostMapping("/signup")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<UsuarioDTO> create(
            @RequestBody @Valid UsuarioDTORequest usuarioDTORequest
    ) throws RestException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        usuarioService.guardar(usuarioDTORequest)
                );
    }

    @ApiOperation(value = "Consulta el usuario autenticado actual",
            response = UsuarioDTO.class,
            produces = "application/json",
            httpMethod = "GET")
    @GetMapping("/usuario")
    public ResponseEntity<UsuarioDTO> userInfo(Authentication authentication) throws RestException{
        return ResponseEntity.ok().body(usuarioService.userInfo(authentication));
    }

    /*
     * Sube la imagen de un usuario
     */

    @PostMapping("/upload")
    public ResponseEntity<?> upload(
            @RequestParam("image") MultipartFile image,
            Authentication authentication)
            throws RestException{
        // TODO: PASAR LOGICA DE NEGOCIOS AL SERVICE
        Map<String, Object> response = new HashMap<>();
        Usuario usuario = usuarioService.findByUsername(authentication.getName());
        if(!image.isEmpty()) {
            String nombreImage =
                    UUID.randomUUID().toString()+"_"+image.getOriginalFilename().replace(" ", "");
            Path path = Paths.get("uploads").resolve(nombreImage).toAbsolutePath();
            try {
                Files.copy(image.getInputStream(), path);
            }catch (IOException e) {
                response.put("Error", e.getMessage().concat(e.getCause().getMessage()));
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            String oldImage = usuario.getImage();

            if(oldImage != null && oldImage.length() > 0 && !oldImage.startsWith("http")) {
                Path oldPath = Paths.get("uploads").resolve(oldImage).toAbsolutePath();
                File oldFileImage = oldPath.toFile();
                if(oldFileImage.exists() && oldFileImage.canRead()) {
                    oldFileImage.delete();
                }
            }

            usuario.setImage(nombreImage);
            usuarioService.actualizar(usuario);
            response.put("usuario", usuario);
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }



    @ApiOperation(value = "Actualiza un usuario",
            response = Usuario.class,
            produces = "application/json",
            httpMethod = "PUT")
    @PutMapping("/usuario")
    public ResponseEntity<Usuario> update(Authentication authentication, @RequestBody Usuario usuario) throws RestException {
        // TODO: PASAR LOGICA DE NEGOCIOS AL SERVICE
        try {
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
            Usuario usuarioFind = usuarioService.findByUsername(authentication.getName());
            if(usuarioFind == null) {
                throw new NotFoundException(
                        ErrorDto.builder()
                                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                                .message(ConstUtil.MESSAGE_NOT_FOUND)
                                .status(HttpStatus.NOT_FOUND.value())
                                .date(LocalDateTime.now())
                                .build()
                );
            }
            usuarioFind.setNombre(usuario.getNombre());
            usuarioFind.setApellido(usuario.getApellido());
            usuarioFind.setFechaNacimiento(usuario.getFechaNacimiento());
            if(usuario.getPassword() != null) {
                usuarioFind.setPassword(passwordEncoder.encode(usuario.getPassword()));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.actualizar(usuarioFind));
        }catch (BadRequestException ex){
            throw ex;
        }catch (Exception ex){
            // log.error("Error {}", ex);
            throw new InternalServerErrorException(
                    ErrorDto.builder()
                            .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                            .message(ConstUtil.MESSAGE_GENERAL)
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .date(LocalDateTime.now())
                            .build()
            );
        }
    }

    /**
     * Obtiene la imagen de un usuario
     * @param name
     * @return
     */
    //@Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/uploads/img/{name:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String name) throws InternalServerErrorException {
        // TODO: MIGRAR LOGICA A SERVICE
        Path path = Paths.get("uploads").resolve(name).toAbsolutePath();
        Resource resource = null;
        try {
            resource = new UrlResource(path.toUri());
            if(!resource.exists()) {
                try {
                    path = Paths.get("uploads").resolve("default.png").toAbsolutePath();
                    resource = new UrlResource(path.toUri());
                }catch(MalformedURLException ex) {
                    throw new InternalServerErrorException(
                            ErrorDto.builder()
                                    .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                                    .message(ex.getMessage())
                                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                    .date(LocalDateTime.now())
                                    .build());
                }
            }
        }catch (MalformedURLException e) {
            throw new InternalServerErrorException(
                    ErrorDto.builder()
                            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                            .message(e.getMessage())
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .date(LocalDateTime.now())
                            .build());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ resource.getFilename() + "\"");
        return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
    }
}
