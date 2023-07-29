package co.edo.iudigital.helpmeiud.controller;

import co.edo.iudigital.helpmeiud.dto.CasoDTO;
import co.edo.iudigital.helpmeiud.exceptions.RestException;
import co.edo.iudigital.helpmeiud.model.Caso;
import co.edo.iudigital.helpmeiud.service.iface.ICasoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/casos")
@Api(value = "/casos", tags = {"Casos"})
@SwaggerDefinition(tags = {
        @Tag(name = "Casos", description = "Gestion API Casos")
})
public class CasoController {

    @Autowired
    private ICasoService casoService;

    @ApiOperation(value = "Obtiene todos casos",
            responseContainer = "List",
            produces = "application/json",
            httpMethod = "GET")
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<List<CasoDTO>> index() {
        return ResponseEntity
                .ok()
                .body(
                        casoService.consultarTodos()
                );
    }
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @ApiOperation(value = "Crea un caso",
            response = Caso.class,
            responseContainer = "Caso",
            produces = "application/json",
            httpMethod = "POST")
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Caso> create(
            @RequestBody @Valid CasoDTO casoDTO
    ) throws RestException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        casoService.crear(casoDTO)
                );
    }
}
