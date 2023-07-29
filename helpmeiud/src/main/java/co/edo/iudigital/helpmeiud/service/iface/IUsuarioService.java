package co.edo.iudigital.helpmeiud.service.iface;

import co.edo.iudigital.helpmeiud.dto.request.UsuarioDTORequest;
import co.edo.iudigital.helpmeiud.dto.response.UsuarioDTO;
import co.edo.iudigital.helpmeiud.exceptions.RestException;
import co.edo.iudigital.helpmeiud.model.Usuario;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface IUsuarioService {

    List<UsuarioDTO> consultarTodos();

    UsuarioDTO consultarPorId(Long id);

    UsuarioDTO consultarPorUsername(String username);

    UsuarioDTO guardar(UsuarioDTORequest usuarioDTORequest) throws RestException;

    Usuario findByUsername(String username);

    UsuarioDTO userInfo(Authentication authentication) throws RestException;

    Usuario actualizar(Usuario usuario) throws RestException;
}
