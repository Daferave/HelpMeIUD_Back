package co.edo.iudigital.helpmeiud.repository;

import co.edo.iudigital.helpmeiud.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUsuarioRepository
        extends JpaRepository<Usuario, Long> {
    Usuario findByUsername(String username);

}
