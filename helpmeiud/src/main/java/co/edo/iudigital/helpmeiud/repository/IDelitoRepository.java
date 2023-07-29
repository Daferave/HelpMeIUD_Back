package co.edo.iudigital.helpmeiud.repository;

import co.edo.iudigital.helpmeiud.model.Delito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // opcional esta anotación
public interface IDelitoRepository
        extends JpaRepository<Delito, Long> {

    List<Delito> findByNombre(String nombre);
}
