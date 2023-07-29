package co.edo.iudigital.helpmeiud.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DelitoDTORequest {


    @NotNull(message = "Nombre es obligatorio")
    @NotEmpty(message = "Nombre es obligatorio")
    String nombre;

    String descripcion;

    @NotNull(message = "Debe proporcionar el ID del usuario")
    @JsonProperty("usuario_id")
    Long usuarioId;
}
