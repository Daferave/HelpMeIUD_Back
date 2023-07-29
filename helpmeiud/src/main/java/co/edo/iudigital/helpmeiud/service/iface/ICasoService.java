package co.edo.iudigital.helpmeiud.service.iface;

import co.edo.iudigital.helpmeiud.dto.CasoDTO;
import co.edo.iudigital.helpmeiud.exceptions.RestException;
import co.edo.iudigital.helpmeiud.model.Caso;

import java.util.List;

public interface ICasoService {

    List<CasoDTO> consultarTodos();

    Caso crear(CasoDTO casoDTO) throws RestException;

    Boolean visible(Boolean visible, Long id);

    CasoDTO consultarPorId(Long id);
}
