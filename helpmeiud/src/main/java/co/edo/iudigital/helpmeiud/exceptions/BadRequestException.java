package co.edo.iudigital.helpmeiud.exceptions;
/**
 * Exception de BadRequest en el Rest
 *
 */
public class BadRequestException extends RestException{
    private static final long serialVersionUID = 1L;

    public BadRequestException() {
        super();
    }

    public BadRequestException(ErrorDto errorDto) {
        super(errorDto);
    }

    public BadRequestException(String msg) {
        super(msg);
    }
}
