package co.edo.iudigital.helpmeiud.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UsuarioDTO {

    Long id;

    String username;

    String nombre;

    String apellido;

    Boolean redSocial;

    LocalDate fechaNacimiento;

    Boolean enabled;

    String image;

    List<String> roles;
}
