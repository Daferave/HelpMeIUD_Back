package co.edo.iudigital.helpmeiud.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CasoDTO {

    Long id;

    @JsonProperty("fecha_hora")
    LocalDateTime fechaHora;

    Float latitud;

    Float longitud;

    Float altitud;

    String descripcion;

    Boolean esVisible;

    @JsonProperty("url_map")
    String urlMap;

    @JsonProperty("rmi_url")
    String rmiUrl;

    @JsonProperty("usuario_id")
    Long usuarioId;

    @JsonProperty("delito_id")
    Long delitoId;
}
