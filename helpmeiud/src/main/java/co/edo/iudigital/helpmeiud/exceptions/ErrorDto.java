package co.edo.iudigital.helpmeiud.exceptions;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Error Dto
 * 
 */
@Getter
@Setter
@Builder
public class ErrorDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String error;

    private String message;

    private int status;

    private LocalDateTime date;

    /**
     * Obtiene nuevo error
     *
     * @param error String Nombre error HTTP
     * @param message String Mensaje personalizado del error HTTP
     * @param status int Codigo error HTTP
     * @return
     */
    /*public static ErrorDto getErrorDto(String error, String message, int status) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError(error);
        errorDto.setMessage(message);
        errorDto.setStatus(status);
        errorDto.setDate(LocalDateTime.now());
        return errorDto;
    }*/
}
